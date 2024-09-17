package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Giocatore;

public interface GiocatoreRepository extends CrudRepository<Giocatore, Long> {
	
	
	public Giocatore save(Giocatore giocatore);
	
	public boolean existsByNomeAndCognomeAndDataDiNascitaAndRuolo(String nome, String cognome, LocalDate dataDiNascita, String ruolo);
	
	public List<Giocatore> findAll();
	
}
