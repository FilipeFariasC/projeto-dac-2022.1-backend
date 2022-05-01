package br.edu.ifpb.dac.groupd.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtRequestFilter requestFilter;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
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
		String login = "/login";
		
		http
			.csrf()
				.disable()
			.exceptionHandling()
				.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
			.and()
				.authorizeRequests()
					.antMatchers("/").permitAll()
					.antMatchers(login).permitAll()
					.antMatchers("/api/signin", "/api/users", "/api/login").permitAll()
					.antMatchers("/api/users/**").authenticated()
					.antMatchers(HttpMethod.PUT ,"/api/users").authenticated()
					.anyRequest().authenticated()
			.and()
				.formLogin(
					form ->
						form
						.loginPage(login)
						.defaultSuccessUrl("/")
						.failureUrl("/login?error=true")
				)
				.logout(
					logout ->
						logout
						.logoutUrl("/logout")
						.logoutSuccessUrl(login)
						.invalidateHttpSession(true)
				);
		
		http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
