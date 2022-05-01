package br.edu.ifpb.dac.groupd.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web
			.ignoring()
			.antMatchers("/resources/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		String login = "/login";
		
		http.authorizeRequests()
			.antMatchers("/").permitAll()
			.antMatchers(login).permitAll()
			.antMatchers("/api/**").permitAll() // TODO
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
	}
}
