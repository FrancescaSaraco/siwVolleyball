package it.uniroma3.siw.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Tesseramento;
import it.uniroma3.siw.repository.TesseramentoRepository;

@Service
public class TesseramentoService {
	
	@Autowired
	TesseramentoRepository tesseramentoRepository;
	@Autowired
	GiocatoreService giocatoreService;
	
	@Transactional
	public Tesseramento save(Tesseramento tesseramento) {
		return this.tesseramentoRepository.save(tesseramento);
	}
	
	public boolean existsByGiocatoreAndInizioTesseramentoAndFineTesseramento(Giocatore giocatore, LocalDate inizioTesseramento, LocalDate fineTesseramento) {
		return this.tesseramentoRepository.existsByGiocatoreAndInizioTesseramentoAndFineTesseramento(giocatore, inizioTesseramento, fineTesseramento);
	}
	
	public boolean existsByGiocatore(Giocatore giocatore) {
		return this.tesseramentoRepository.existsByGiocatore(giocatore);
	}
	
	public List<Tesseramento> findAllByGiocatore(Giocatore giocatore) {
		return this.tesseramentoRepository.findAllByGiocatore(giocatore);
	}
	
	public List<Tesseramento> findAll() {
		return this.tesseramentoRepository.findAll();
	}
	
	public boolean isTesserato(Giocatore giocatore) {
		LocalDate oggi = LocalDate.now();
		List<Tesseramento> tesseramentiDiQuelGiocatore = this.findAllByGiocatore(giocatore);
		for(Tesseramento tesseramento : tesseramentiDiQuelGiocatore) {
			if(oggi.isBefore(tesseramento.getFineTesseramento()) && oggi.isAfter(tesseramento.getInizioTesseramento())) {
				return true;
			}		
		}
		return false;
	}
	
	public List<Giocatore> findRimasti() {
		List<Giocatore> rimasti = new ArrayList<>();
		List<Giocatore> tutti = this.giocatoreService.findAll();
		
		for(Giocatore giocatore : tutti) {
			if(!this.isTesserato(giocatore)) {
				rimasti.add(giocatore);
			}
		}
		return rimasti;
	}

}
