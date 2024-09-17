package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.PresidenteRepository;

@Service
public class PresidenteService {
	
	@Autowired
	PresidenteRepository presidenteRepository;
	
	@Transactional
	public Presidente save(Presidente presidente) {
		return this.presidenteRepository.save(presidente);
	}
	
	public boolean existsByCodiceFiscale(String codiceFiscale) {
		return this.presidenteRepository.existsByCodiceFiscale(codiceFiscale);
	}
	
	public List<Presidente> findAll() {
		return this.presidenteRepository.findAll();
	}
	
	public Presidente findByCodiceFiscale(String codiceFiscale) {
		return this.presidenteRepository.findByCodiceFiscale(codiceFiscale);
	}
	
	public Optional<Presidente> findById(Long id) {
		return this.presidenteRepository.findById(id);
	}
	
	public Presidente findByUtente(Utente utente) {
		return this.presidenteRepository.findByUtente(utente);
	}
	
	public List<Presidente> findRimasti() {
		List<Presidente> senzaSquadra = new ArrayList<>();
		List<Presidente> tutti = this.findAll();
		for(Presidente presidente : tutti) {
			if(presidente.getSquadra() == null) {
				senzaSquadra.add(presidente);
			}
		}
		return senzaSquadra;
	}
}
