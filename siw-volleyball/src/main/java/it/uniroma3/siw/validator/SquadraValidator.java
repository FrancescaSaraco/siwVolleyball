package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.repository.SquadraRepository;


@Component
public class SquadraValidator implements Validator {
	@Autowired
	private SquadraRepository squadraRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Squadra squadra = (Squadra)o;
		if (squadra.getNome() != null 
				&& squadraRepository.existsByNome(squadra.getNome())) {
			errors.reject("Squadra.duplicate");
		}
	}
	
	@Override
	public boolean supports(Class<?> aClass) {
		return Squadra.class.equals(aClass);
	}
}