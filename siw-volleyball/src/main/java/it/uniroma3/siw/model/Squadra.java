package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Squadra {
	
	@Column(unique=true)
	public String nome;
	
	private LocalDate annoFondazione;
	private String indirizzo;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@OneToMany(mappedBy="squadra")
	private List<Tesseramento> tesseramenti;
	
	@OneToOne(mappedBy="squadra")
	private Presidente presidente;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getAnnoFondazione() {
		return annoFondazione;
	}

	public void setAnnoFondazione(LocalDate annoFondazione) {
		this.annoFondazione = annoFondazione;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Tesseramento> getTesseramenti() {
		return tesseramenti;
	}

	public void setTesseramenti(List<Tesseramento> tesseramenti) {
		this.tesseramenti = tesseramenti;
	}

	public Presidente getPresidente() {
		return presidente;
	}

	public void setPresidente(Presidente presidente) {
		this.presidente = presidente;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Squadra other = (Squadra) obj;
		return Objects.equals(nome, other.nome);
	}

	
	
	
	
	
	
}
