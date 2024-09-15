package fr.nilpo.pan.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import fr.nilpo.pan.config.AuthService;
import fr.nilpo.pan.models.users.AppUser;
import fr.nilpo.pan.models.users.AppUserRepository;

@Controller
public class AccountController {
	
	@Autowired
	private AuthService authService;

	@Autowired
	private PasswordEncoder pwdEncoder;
	
	@Autowired
	private AppUserRepository userRepository;
	
	@GetMapping("/signin")
	public String signin(Model model) {
		var user = new AppUser();
		model.addAttribute("user", user);
		
		return "/account/signin";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		var user = new AppUser();
		model.addAttribute("user", user);
		model.addAttribute("passwordConfirmation", "");
		
		return "/account/signup";
	}
	
	@PostMapping("/signup")
	public String signup(
			@ModelAttribute("user") @Valid AppUser user, 
			BindingResult results) {
		if (results.hasErrors()) {
			return "/account/signup";
		}
		
		user.setPassword(pwdEncoder.encode(user.getPassword()));
		user.setPasswordConfirmation(user.getPassword());
		user.setRole(AppUser.Role.USER);
		
		userRepository.save(user);
		
		authService.authenticate(user);
		
		return "redirect:/";
	}
}
