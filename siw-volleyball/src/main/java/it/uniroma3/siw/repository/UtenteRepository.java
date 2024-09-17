package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Utente;

public interface UtenteRepository extends CrudRepository<Utente, Long> {

	
	public boolean existsByEmail(String email);
	
	public Utente findByEmail(String email);
	
}
