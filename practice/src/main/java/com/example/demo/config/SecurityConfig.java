package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeRequests(authorize -> authorize
						.requestMatchers("/admin/signup", "/admin/signin").permitAll()
						.requestMatchers("/admin/**").authenticated()
						.anyRequest().permitAll())
				.formLogin(form -> form
						.loginPage("/admin/signin")
						.loginProcessingUrl("/admin/signin")
						.defaultSuccessUrl("/admin/contacts", true)
						.usernameParameter("email")
						.passwordParameter("password")
						.permitAll()
						.failureUrl("/admin/signin?error=true")
						.successHandler((request, response, authentication) -> {
							System.out.println("Login successful: " + authentication.getName());
							response.sendRedirect("/admin/contacts");
						})
						.failureHandler((request, response, exception) -> {
							System.out.println("Login failed: " + exception.getMessage());
							response.sendRedirect("/admin/signin?error=true");
						}))
				.logout(logout -> logout
						.logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout"))
						.logoutSuccessUrl("/admin/signin?logout")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID"))
				.csrf().disable();

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
