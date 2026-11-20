package fr.nilpo.pan.config;

import java.io.IOException;
import java.time.Duration;
import java.util.Comparator;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Keeps the number of sessions bounded. Sessions are stored in PostgreSQL
 * (Spring Session JDBC), not in the heap, but nothing would stop a client
 * from creating them endlessly:
 * <ul>
 * <li>an anonymous session (CSRF token of the signin/signup form, request
 * saved before /signin) expires after {@link #ANONYMOUS_TIMEOUT} of
 * inactivity;</li>
 * <li>a logged-in session gets {@link #AUTHENTICATED_TIMEOUT}, and logging in
 * deletes the user's least recently used sessions beyond
 * {@link #MAX_SESSIONS_PER_USER}. Without this, replaying a remember-me
 * cookie without the session cookie would create one session per request.
 * The sessions are really deleted from the store: Spring Security's
 * maximumSessions() only marks them expired, and they would stay until
 * their timeout.</li>
 * </ul>
 *
 * Runs before Spring Security (and inside Spring Session's filter), after the
 * rest of the chain: the login and logout filters end the chain themselves,
 * so a filter placed after them would never see a login. The policy is
 * applied when the session is first seen with a logged-in user, whatever the
 * way in (login form, signup, remember-me), using a session attribute as the
 * marker.
 */
public class SessionPolicyFilter extends OncePerRequestFilter {

	static final Duration ANONYMOUS_TIMEOUT = Duration.ofMinutes(5);
	static final Duration AUTHENTICATED_TIMEOUT = Duration.ofMinutes(30);
	static final int MAX_SESSIONS_PER_USER = 5;

	/** The user the policy was last applied for, in this session. */
	static final String POLICY_USER_ATTRIBUTE = SessionPolicyFilter.class.getName() + ".USER";

	private final FindByIndexNameSessionRepository<? extends Session> sessions;

	public SessionPolicyFilter(FindByIndexNameSessionRepository<? extends Session> sessions) {
		this.sessions = sessions;
	}

	/** Static files, photos and client error reports never use the session: don't load it for them. */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		var path = request.getServletPath();
		return path.startsWith("/bundle/") || path.startsWith("/img/") || path.startsWith("/photos/")
				|| path.equals("/api/errors");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		chain.doFilter(request, response);
		applyPolicy(request);
	}

	private void applyPolicy(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) return;

		String username = loggedInUsername(session);
		if (username == null) {
			setTimeout(session, ANONYMOUS_TIMEOUT);
			return;
		}

		if (!username.equals(session.getAttribute(POLICY_USER_ATTRIBUTE))) {
			// First request of this session as a logged-in session.
			session.setAttribute(POLICY_USER_ATTRIBUTE, username);
			setTimeout(session, AUTHENTICATED_TIMEOUT);
			deleteOldestSessions(username, session.getId());
		}
	}

	private static String loggedInUsername(HttpSession session) {
		var attribute = session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
		if (!(attribute instanceof SecurityContext context)) return null;

		var auth = context.getAuthentication();
		if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) return null;

		// The name Spring Session indexes sessions by (PRINCIPAL_NAME).
		return auth.getName();
	}

	private static void setTimeout(HttpSession session, Duration timeout) {
		if (session.getMaxInactiveInterval() != timeout.toSeconds()) {
			session.setMaxInactiveInterval((int) timeout.toSeconds());
		}
	}

	/** Keeps the current session and the user's most recently used other ones, up to the limit. */
	private void deleteOldestSessions(String username, String currentSessionId) {
		sessions.findByPrincipalName(username).values().stream()
				.filter(s -> !s.getId().equals(currentSessionId))
				.sorted(Comparator.comparing(Session::getLastAccessedTime).reversed())
				.skip(MAX_SESSIONS_PER_USER - 1)
				.forEach(s -> sessions.deleteById(s.getId()));
	}

	@Configuration
	static class Registration {

		/** Registered here rather than as a bean of its own, to control its position among the servlet filters. */
		@Bean
		FilterRegistrationBean<SessionPolicyFilter> sessionPolicyFilter(
				FindByIndexNameSessionRepository<? extends Session> sessions) {
			var registration = new FilterRegistrationBean<>(new SessionPolicyFilter(sessions));
			// Right inside Spring Session's filter, well before Spring Security's (-100).
			registration.setOrder(SessionRepositoryFilter.DEFAULT_ORDER + 1);
			return registration;
		}
	}
}
