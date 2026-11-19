package fr.nilpo.pan.config;

import java.io.IOException;
import java.time.Duration;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Brute-force protection for the login form: failed attempts are counted
 * per (username, client IP) and per client IP; once a limit is reached,
 * further attempts are refused until the window expires. A successful
 * login resets the per-account counter.
 *
 * The per-account limit is keyed by IP too, so an attacker can't lock a
 * victim out from everywhere just by failing on their username.
 */
@Component
public class LoginThrottle {

	static final int MAX_FAILURES_PER_ACCOUNT_AND_IP = 10;
	static final int MAX_FAILURES_PER_IP = 50;
	static final Duration WINDOW = Duration.ofMinutes(15);

	private static final int MAX_USERNAME_KEY_LENGTH = 100;

	private final RateLimiter rateLimiter;

	public LoginThrottle(RateLimiter rateLimiter) {
		this.rateLimiter = rateLimiter;
	}

	public boolean isBlocked(HttpServletRequest request) {
		return rateLimiter.isLimited(ipKey(request), MAX_FAILURES_PER_IP)
				|| rateLimiter.isLimited(accountKey(request), MAX_FAILURES_PER_ACCOUNT_AND_IP);
	}

	public void onFailure(HttpServletRequest request) {
		rateLimiter.record(ipKey(request), WINDOW);
		rateLimiter.record(accountKey(request), WINDOW);
	}

	public void onSuccess(HttpServletRequest request) {
		rateLimiter.reset(accountKey(request));
	}

	private static String ipKey(HttpServletRequest request) {
		return "login-ip|" + request.getRemoteAddr();
	}

	private static String accountKey(HttpServletRequest request) {
		var username = request.getParameter("username");
		if (username == null) username = "";
		if (username.length() > MAX_USERNAME_KEY_LENGTH) username = username.substring(0, MAX_USERNAME_KEY_LENGTH);
		return "login-account|" + request.getRemoteAddr() + "|" + username;
	}

	/** Rejects POST /signin while the client is blocked. Registered in SecurityConfig (not as a bean, to avoid a second servlet registration). */
	public static class Filter extends OncePerRequestFilter {
		private final LoginThrottle throttle;

		public Filter(LoginThrottle throttle) {
			this.throttle = throttle;
		}

		@Override
		protected boolean shouldNotFilter(HttpServletRequest request) {
			return !("POST".equals(request.getMethod()) && "/signin".equals(request.getServletPath()));
		}

		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
				throws ServletException, IOException {
			if (throttle.isBlocked(request)) {
				response.sendRedirect(request.getContextPath() + "/signin?blocked");
				return;
			}
			chain.doFilter(request, response);
		}
	}
}
