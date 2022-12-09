package fr.ffcam.pan.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.ffcam.pan.models.PanEntity;
import fr.ffcam.pan.models.users.AppUser;

@Service
public class AuthService {
	public AppUser getCurrentUser() {
		var auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth == null) return null;
		
		try {
			return (AppUser) auth.getPrincipal();
		} catch (ClassCastException e) {
			return null;
		}
	}
	
	public void authenticate(AppUser user) {
		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken(user, user.getPassword(), null)
		);
	}
	
	public boolean canUpdate(PanEntity entity) {
		var user = getCurrentUser();
		return user != null &&
				(user.getRole() == AppUser.Role.ADMIN || user.getId().equals(entity.getCreatedBy().getId()));
			
	}
}
