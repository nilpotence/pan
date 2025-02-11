package fr.nilpo.pan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Value("${pan.remember-me-key}")
	private String REMEMBER_ME_KEY;
	
	@Autowired
	private UserDetailsService usersDetailsService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.sessionManagement(sessionManagement -> sessionManagement
					.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
			)
			.authorizeHttpRequests(authorize -> authorize
					.antMatchers("/boulders/new", "/boulders/*/edit")
					.authenticated()
					.antMatchers(HttpMethod.GET, "/", "/boulders", "/boulders/**","/api/errors", "/img/**", "/bundle/**", "/icons/**")
					.permitAll()
					.antMatchers("/signin", "/signup")
					.permitAll()
					.anyRequest()
					.authenticated()
			)
			.formLogin(form -> form
					.loginPage("/signin")
					.permitAll()
			)
			.logout(logout -> logout
					.logoutUrl("/signout")
					.logoutSuccessUrl("/")
					.permitAll()
			)
			.rememberMe()
				.key(REMEMBER_ME_KEY)
				.userDetailsService(usersDetailsService);
				
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
