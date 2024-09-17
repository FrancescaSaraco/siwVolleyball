package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.repository.GiocatoreRepository;


@Component
public class GiocatoreValidator implements Validator {
	
	@Autowired
	private GiocatoreRepository giocatoreRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Giocatore giocatore = (Giocatore)o;
		if ((giocatore.getNome() != null && giocatore.getCognome() != null && giocatore.getRuolo() != null 
			&& giocatore.getCittaNascita() != null && giocatore.getDataDiNascita() != null)
			&& (giocatoreRepository.existsByNomeAndCognomeAndDataDiNascitaAndRuolo(giocatore.getNome(), 
						giocatore.getCognome(), giocatore.getDataDiNascita(), giocatore.getRuolo()))) {
			errors.reject("Giocatore.duplicate");
		}
	}
	
	@Override
	public boolean supports(Class<?> aClass) {
		return Giocatore.class.equals(aClass);
	}
}