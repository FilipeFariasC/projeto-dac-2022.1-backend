package br.edu.ifpb.dac.groupd.business.service.interfaces;

import org.springframework.security.crypto.password.PasswordEncoder;

import br.edu.ifpb.dac.groupd.model.entities.User;

public interface PasswordEncoderService extends PasswordEncoder {
	
	void encryptPassword(User user);
}
