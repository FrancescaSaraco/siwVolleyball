package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.repository.CredenzialiRepository;
import it.uniroma3.siw.repository.PresidenteRepository;
import it.uniroma3.siw.service.CredenzialiService;




@Component
public class PresidenteValidator implements Validator {
	@Autowired
	private PresidenteRepository presidenteRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Presidente presidente = (Presidente)o;
		if (presidente.getCodiceFiscale() != null 
				&& presidenteRepository.existsByCodiceFiscale(presidente.getCodiceFiscale())) {
			errors.reject("Presidente.duplicate");
		}
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Presidente.class.equals(aClass);
	}
}