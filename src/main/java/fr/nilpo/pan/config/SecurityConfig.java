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
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;

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
	
	/**
	 * No inline or third-party scripts: an injected <script> or event
	 * handler would not run. Inline style attributes are still used by the
	 * templates; photos are data: URLs and the photo picker previews a blob:.
	 */
	static final String CONTENT_SECURITY_POLICY = String.join("; ",
			"default-src 'self'",
			"script-src 'self'",
			"style-src 'self' 'unsafe-inline'",
			"img-src 'self' data: blob:",
			"font-src 'self' data:",
			"connect-src 'self'",
			"object-src 'none'",
			"base-uri 'self'",
			"form-action 'self'",
			"frame-ancestors 'none'");
	
	@Bean
	public SecurityContextRepository securityContextRepository() {
		return new HttpSessionSecurityContextRepository();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, SecurityContextRepository securityContextRepository) throws Exception {
		http
			.headers(headers -> headers
					.contentSecurityPolicy(csp -> csp.policyDirectives(CONTENT_SECURITY_POLICY))
					.referrerPolicy(referrer -> referrer.policy(ReferrerPolicy.SAME_ORIGIN))
			)
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
					.requestMatchers(HttpMethod.GET, "/", "/boulders", "/boulders/**","/api/errors", "/img/**", "/bundle/**")
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
