package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Tesseramento;

public interface TesseramentoRepository extends CrudRepository<Tesseramento, Long> {
	
	
	public Tesseramento save(Tesseramento tesseramento);
	
	public List<Tesseramento> findAll();
	
	public boolean existsByGiocatoreAndInizioTesseramentoAndFineTesseramento(Giocatore giocatore, LocalDate inizioTesseramento, LocalDate fineTesseramento);
	
	public boolean existsByGiocatore(Giocatore giocatore);
	
	public List<Tesseramento> findAllByGiocatore(Giocatore giocatore);
	
	public Tesseramento findByGiocatore(Giocatore giocatore);
	
}
