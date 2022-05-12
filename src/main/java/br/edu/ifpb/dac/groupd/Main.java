package br.edu.ifpb.dac.groupd;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Main {
	public static void main(String[] args) {
		String value = "abc";
		BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
		value = passEncoder.encode(value);
		
		
		System.out.println(value.matches("^\\$2[ayb]\\$.{56}$"));
	}
}	
