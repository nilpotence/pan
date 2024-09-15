package fr.nilpo.pan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import fr.nilpo.pan.config.AuthService;
import fr.nilpo.pan.models.users.AppUser;

@ControllerAdvice
public class PanControllerAdvice {
	
	@Autowired
	private AuthService authService;
	
	@ModelAttribute("currentUser")
	public AppUser getCurrentUser() {
		return authService.getCurrentUser();
	}
}
