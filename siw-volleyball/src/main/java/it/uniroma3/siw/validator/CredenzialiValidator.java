package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.repository.CredenzialiRepository;

@Component
public class CredenzialiValidator implements Validator {

	@Autowired
	private CredenzialiRepository credenzialiRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Credenziali cred = (Credenziali)o;
		if (cred.getUsername()!=null 
				&& credenzialiRepository.existsByUsername(cred.getUsername())){
			errors.reject("Credenziali.duplicate");
		}
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Credenziali.class.equals(aClass);
	}
	
}
