package fr.nilpo.pan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

	@Value("${pan.remember-me-key}")
	private String REMEMBER_ME_KEY;
	
	@Autowired
	private UserDetailsService usersDetailsService;
	
	@Autowired
	private LoginThrottle loginThrottle;
	
	@Bean
	public SecurityContextRepository securityContextRepository() {
		return new HttpSessionSecurityContextRepository();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, SecurityContextRepository securityContextRepository) throws Exception {
		http
			.securityContext(securityContext -> securityContext
					.securityContextRepository(securityContextRepository)
			)
			.sessionManagement(sessionManagement -> sessionManagement
					.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
			)
			.authorizeHttpRequests(authorize -> authorize
					// Since Spring Security 6 the rules also apply to error
					// dispatches and forwards: let error pages render for
					// anonymous users instead of redirecting them to /signin.
					.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR)
					.permitAll()
					.requestMatchers("/boulders/new", "/boulders/*/edit")
					.authenticated()
					.requestMatchers(HttpMethod.GET, "/", "/boulders", "/boulders/**","/api/errors", "/img/**", "/bundle/**", "/icons/**")
					.permitAll()
					.requestMatchers("/signin", "/signup")
					.permitAll()
					.anyRequest()
					.authenticated()
			)
			.addFilterBefore(new LoginThrottle.Filter(loginThrottle), UsernamePasswordAuthenticationFilter.class)
			.formLogin(form -> form
					.loginPage("/signin")
					.permitAll()
					.failureHandler((request, response, exception) -> {
						loginThrottle.onFailure(request);
						response.sendRedirect(request.getContextPath() + "/signin?error");
					})
					.successHandler(new SavedRequestAwareAuthenticationSuccessHandler() {
						@Override
						public void onAuthenticationSuccess(
								jakarta.servlet.http.HttpServletRequest request,
								jakarta.servlet.http.HttpServletResponse response,
								org.springframework.security.core.Authentication authentication)
								throws java.io.IOException, jakarta.servlet.ServletException {
							loginThrottle.onSuccess(request);
							super.onAuthenticationSuccess(request, response, authentication);
						}
					})
			)
			.logout(logout -> logout
					.logoutUrl("/signout")
					.logoutSuccessUrl("/")
					.permitAll()
			)
			.rememberMe(rememberMe -> rememberMe
					.key(REMEMBER_ME_KEY)
					.userDetailsService(usersDetailsService)
			);
				
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
