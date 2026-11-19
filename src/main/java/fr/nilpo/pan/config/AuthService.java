package fr.nilpo.pan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import fr.nilpo.pan.models.PanEntity;
import fr.nilpo.pan.models.users.AppUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {
	
	@Autowired
	private SecurityContextRepository securityContextRepository;
	
	public AppUser getCurrentUser() {
		var auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth == null) return null;
		
		try {
			return (AppUser) auth.getPrincipal();
		} catch (ClassCastException e) {
			return null;
		}
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
		
		var context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(
			new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities())
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
