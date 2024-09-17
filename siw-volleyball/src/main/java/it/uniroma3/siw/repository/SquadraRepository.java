package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Squadra;

public interface SquadraRepository extends CrudRepository<Squadra, Long> {
	
	
	public List<Squadra> findAll();
	
	public Squadra save(Squadra squadra);
	
	public boolean existsByNome(String nome);
	
	public Optional<Squadra> findById(Long id);
}
