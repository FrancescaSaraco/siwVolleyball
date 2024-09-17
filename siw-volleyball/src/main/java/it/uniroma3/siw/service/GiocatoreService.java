package it.uniroma3.siw.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.repository.GiocatoreRepository;
import it.uniroma3.siw.repository.TesseramentoRepository;

@Service
public class GiocatoreService {
	
	@Autowired		// serve per creare un oggetto Giocatore da manipolare in questa classe, come se fosse un costruttore
	GiocatoreRepository giocatoreRepository;
	@Autowired
	TesseramentoRepository tesseramentoRepository;
	
	
	
	@Transactional // usa @Transactional quando il metodo modifica lo stato del database
	public Giocatore save(Giocatore giocatore) {
		return this.giocatoreRepository.save(giocatore);
	}
	
	public boolean existsByNomeAndCognomeAndDataDiNascitaAndRuolo(String nome, String cognome, LocalDate dataDiNascita, String ruolo) {
		return this.giocatoreRepository.existsByNomeAndCognomeAndDataDiNascitaAndRuolo(nome, cognome, dataDiNascita, ruolo);
	}
	
	public List<Giocatore> findAll() {
		return this.giocatoreRepository.findAll();
	}
	
	/*public List<Giocatore> findNonTesserati(Squadra squadra) {
		List<Giocatore> rimasti = new ArrayList<>();
		List<Giocatore> tutti = this.findAll();
		
		for(Giocatore giocatore : tutti) {
			if(!this.tesseramentoRepository.existsBySquadraAndGiocatore(squadra, giocatore)) {
				rimasti.add(giocatore);
			}
		}
		return rimasti;
	}*/
	
	
	
}
