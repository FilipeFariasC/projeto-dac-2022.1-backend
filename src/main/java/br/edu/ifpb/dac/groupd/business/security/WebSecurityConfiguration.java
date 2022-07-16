package br.edu.ifpb.dac.groupd.business.security;


import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.*;
import static br.edu.ifpb.dac.groupd.business.service.RoleService.AVAILABLE_ROLES.*;
import static org.springframework.security.config.http.SessionCreationPolicy.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.edu.ifpb.dac.groupd.business.service.interfaces.PasswordEncoderService;
import br.edu.ifpb.dac.groupd.business.service.interfaces.TokenService;
import br.edu.ifpb.dac.groupd.business.service.interfaces.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtRequestFilter requestFilter;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private PasswordEncoderService passwordEncoder;
	
	private static final String urlFrontend = "http://localhost:4200";
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService)
			.passwordEncoder(passwordEncoder);
	}
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web
			.ignoring()
			.antMatchers("/resources/**");
	}
	
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.cors()
			.and()
			.csrf()
				.disable()
			.exceptionHandling()
				.authenticationEntryPoint(new HttpStatusEntryPoint(UNAUTHORIZED))
			.and()
				.authorizeRequests()
					.antMatchers(GET, "/actuator").permitAll()
					.antMatchers(POST, "/api/signin", "/api/users", "/api/login", "/api/isValidToken").permitAll()
					.antMatchers("/api/users/**",
						"/api/bracelets","/api/bracelets/**", 
						"/api/fences","/api/fences/**", 
						"/api/locations", "/api/locations/**", 
						"/api/alarms/**" ).authenticated()
					.antMatchers(DELETE, "/api/users").hasRole(ADMIN.toString())
					.anyRequest().authenticated()
			.and()
				.sessionManagement()
					.sessionCreationPolicy(STATELESS)
				.and()
				.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class)
				.logout(
					logout ->
						logout
						.clearAuthentication(true)
						.logoutUrl("/api/logout")
						.logoutSuccessUrl("/api/login")
						.invalidateHttpSession(true)
						.logoutSuccessHandler((request, response, authentication) -> {})
				);
	}
	
}
