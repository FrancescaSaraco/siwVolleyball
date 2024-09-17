package it.uniroma3.siw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.repository.SquadraRepository;

@Service
public class SquadraService {
	
	
	@Autowired
	SquadraRepository squadraRepository;
	
	@Transactional
	public Squadra save(Squadra squadra) {
		return this.squadraRepository.save(squadra);
	}
	
	public List<Squadra> findAll() {
		return this.squadraRepository.findAll();
	}
	
	public boolean existsByNome(String nome) {
		return this.squadraRepository.existsByNome(nome);
	}
	
	public Optional<Squadra> findById(Long id) {
		return this.squadraRepository.findById(id);
	}
}
