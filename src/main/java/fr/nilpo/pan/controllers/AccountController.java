package fr.nilpo.pan.controllers;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.nilpo.pan.config.AuthService;
import fr.nilpo.pan.config.RateLimiter;
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
	
	@Autowired
	private RateLimiter rateLimiter;
	
	// Generous: people often sign up from the same climbing gym network.
	static final int MAX_SIGNUPS_PER_IP_PER_HOUR = 30;
	// BCrypt only uses the first 72 bytes of a password.
	static final int MAX_PASSWORD_BYTES = 72;
	
	/**
	 * Only these fields may be bound from the request onto AppUser: binding
	 * "id" would turn save() into a merge onto an existing account, and
	 * "role" would allow self-promotion.
	 */
	@InitBinder("user")
	public void initUserBinder(WebDataBinder binder) {
		binder.setAllowedFields("username", "password", "passwordConfirmation");
	}

	@GetMapping("/signin")
	public String signin(
			Model model,
			@RequestParam(required = false) String error,
			@RequestParam(required = false) String blocked) {
		var user = new AppUser();
		model.addAttribute("user", user);
		model.addAttribute("loginError", error != null);
		model.addAttribute("loginBlocked", blocked != null);
		
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
			BindingResult results,
			HttpServletRequest request,
			HttpServletResponse response) {
		if (user.getPassword() != null && user.getPassword().getBytes(StandardCharsets.UTF_8).length > MAX_PASSWORD_BYTES) {
			results.rejectValue("password", "password.tooLong", "Le mot de passe est trop long !");
		}
		
		if (results.hasErrors()) {
			return "/account/signup";
		}
		
		if (!rateLimiter.tryAcquire("signup|" + request.getRemoteAddr(), MAX_SIGNUPS_PER_IP_PER_HOUR, Duration.ofHours(1))) {
			results.reject("signup.throttled", "Trop de comptes créés depuis votre réseau, réessayez dans une heure.");
			return "/account/signup";
		}
		
		user.setId(null); // always create, never merge onto an existing account
		user.setPassword(pwdEncoder.encode(user.getPassword()));
		user.setPasswordConfirmation(user.getPassword());
		user.setRole(AppUser.Role.USER);
		
		userRepository.save(user);
		user = userRepository
				.findById(user.getId())
				.orElseThrow(() -> new RuntimeException("Erreur lors de la création de l'utilisateur"));
		
		authService.authenticate(user, request, response);
		
		return "redirect:/";
	}
}
