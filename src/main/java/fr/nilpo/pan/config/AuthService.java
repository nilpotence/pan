package fr.nilpo.pan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import fr.nilpo.pan.models.PanEntity;
import fr.nilpo.pan.models.users.AppUser;
import fr.nilpo.pan.models.users.AppUserRepository;
import fr.nilpo.pan.models.users.PanUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {

	private static final String CURRENT_USER_ATTRIBUTE = AuthService.class.getName() + ".CURRENT_USER";

	@Autowired
	private SecurityContextRepository securityContextRepository;

	@Autowired
	private AppUserRepository userRepository;

	/**
	 * The logged-in user, loaded from the database (the session only holds
	 * a PanUserDetails), so its score and rank are up to date. Loaded once
	 * per request: every page asks for it (PanControllerAdvice).
	 *
	 * @return null if nobody is logged in, or if the account no longer exists
	 */
	public AppUser getCurrentUser() {
		var auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !(auth.getPrincipal() instanceof PanUserDetails principal)) return null;

		var request = RequestContextHolder.getRequestAttributes();
		if (request != null
				&& request.getAttribute(CURRENT_USER_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST) instanceof AppUser cached
				&& cached.getId().equals(principal.getId())) {
			return cached;
		}

		var user = userRepository.findById(principal.getId()).orElse(null);
		if (request != null && user != null) {
			request.setAttribute(CURRENT_USER_ATTRIBUTE, user, RequestAttributes.SCOPE_REQUEST);
		}
		return user;
	}

	/**
	 * Logs the user in programmatically (after signup). Since Spring
	 * Security 6 the context is no longer saved to the session
	 * automatically, so it is persisted explicitly; the session id is
	 * rotated first since this bypasses the session fixation protection
	 * that the form login applies.
	 */
	public void authenticate(AppUser user, HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession(false) != null) {
			request.changeSessionId();
		}

		var principal = PanUserDetails.withoutPassword(user);
		var context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(
			UsernamePasswordAuthenticationToken.authenticated(principal, null, principal.getAuthorities())
		);
		SecurityContextHolder.setContext(context);
		securityContextRepository.saveContext(context, request, response);
	}

	public boolean canUpdate(PanEntity entity) {
		var user = getCurrentUser();
		return user != null &&
				(user.getRole() == AppUser.Role.ADMIN || user.getId().equals(entity.getCreatedBy().getId()));

	}
}
