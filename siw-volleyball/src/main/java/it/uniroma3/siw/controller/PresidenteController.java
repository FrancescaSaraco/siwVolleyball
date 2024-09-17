package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Tesseramento;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.GiocatoreRepository;
import it.uniroma3.siw.repository.SquadraRepository;
import it.uniroma3.siw.repository.TesseramentoRepository;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.GiocatoreService;
import it.uniroma3.siw.service.PresidenteService;
import it.uniroma3.siw.service.TesseramentoService;
import it.uniroma3.siw.service.UtenteService;
import it.uniroma3.siw.validator.CredenzialiValidator;
import it.uniroma3.siw.validator.PresidenteValidator;
import it.uniroma3.siw.validator.UtenteValidator;
import jakarta.validation.Valid;

@Controller
public class PresidenteController {
	
	@Autowired
	PresidenteService presidenteService;
	@Autowired
	TesseramentoService tesseramentoService;
	@Autowired
	GiocatoreService giocatoreService;
	@Autowired
	UtenteService utenteService;
	@Autowired
	CredenzialiService credenzialiService;
	@Autowired
	GiocatoreRepository giocatoreRepository;
	@Autowired
	SquadraRepository squadraRepository;
	@Autowired
	TesseramentoRepository tesseramentoRepository;
	@Autowired
	PresidenteValidator presidenteValidator;
	@Autowired
	UtenteValidator utenteValidator;
	@Autowired
	CredenzialiValidator credenzialiValidator;
	
	/*@GetMapping("/presidenti")
	public String listaPresidenti(Model model) {
		model.addAttribute("giocatori", this.presidenteService.findAll());
		return "presidenti.html";
	}*/
	
	@PostMapping("/admin/presidenti") 
	public String NewPresidente(@Valid @ModelAttribute("presidente") Presidente presidente, @ModelAttribute("utente") Utente utente,@ModelAttribute("credenziali") Credenziali credenziali,
			BindingResult utenteResult, BindingResult credenzialiResult, BindingResult presidenteResult, Model model) {
	
		this.presidenteValidator.validate(presidente, presidenteResult);
		this.utenteValidator.validate(utente, utenteResult);
		this.credenzialiValidator.validate(credenziali, credenzialiResult);
		
		if(!presidenteResult.hasErrors() || !utenteResult.hasErrors() || !credenzialiResult.hasErrors()) {
			this.utenteService.saveUser(utente);
			presidente.setUtente(utente);
			credenziali.setUser(utente);
			this.presidenteService.save(presidente);
			this.credenzialiService.saveCredentials(credenziali);
			return "admin/homePageAdmin.html";
		} else {
			model.addAttribute("utente", utente);
			model.addAttribute("credenziali", credenziali);
			model.addAttribute("presidente", presidente);
			return "admin/formNewPresidente.html";
		}
		
	}
	
	// per poter inserire i dati di un presidente devo prima crearlo
	@GetMapping(value = "/admin/formNewPresidente") 
	public String showFormNewPresidente (Model model) {
		model.addAttribute("presidente", new Presidente());
		model.addAttribute("utente", new Utente());
		model.addAttribute("credenziali", new Credenziali());
		return "admin/formNewPresidente.html";
	}
	
	@GetMapping(value = "/presidente/formNewTesseramento")
	public String showFormNewTesseramento(Model model) {
		/*UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Utente utente = this.utenteService.findByEmail(userDetails.getUsername());*/
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credenziali credenziali = credenzialiService.getCredentials(userDetails.getUsername());
		
		//Credenziali credenziali = this.credenzialiService.getCredentials(username);
		
		
		Utente utente = credenziali.getUser();
		
		Presidente corrente = this.presidenteService.findByUtente(utente);	
		Squadra squadra = corrente.getSquadra();
		Tesseramento tesseramento = new Tesseramento();
		
		if(squadra == null) {
			return "presidente/nessunaSquadra.html";
		} else {
			tesseramento.setSquadra(squadra);
			squadra.getTesseramenti().add(tesseramento);
			
			
			model.addAttribute("tesseramento", tesseramento);
			model.addAttribute("disponibili", this.tesseramentoService.findRimasti());
			model.addAttribute("squadra", squadra);
			return "presidente/formNewTesseramento.html";
		}	
	}
	
	@PostMapping("/presidente/nuovoTesseramento/{idSquadra}")
	public String formNewTesseramento(@ModelAttribute("tesseramento") Tesseramento tesseramento, @RequestParam("giocatore") Long id, @PathVariable("idSquadra") Long idS,   Model model) {
		
		tesseramento.setGiocatore(this.giocatoreRepository.findById(id).get());
		tesseramento.setSquadra(this.squadraRepository.findById(idS).get());
		this.tesseramentoService.save(tesseramento);
		tesseramento.getSquadra().getTesseramenti().add(tesseramento);
		return "presidente/homePagePresidente.html";
	}
	
	@GetMapping("/presidente/cancellaGiocatore") 
	public String cancellaGiocatore(Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credenziali credenziali = credenzialiService.getCredentials(userDetails.getUsername());
		
		Utente utente = credenziali.getUser();
		Presidente corrente = this.presidenteService.findByUtente(utente);
		Squadra squadra = corrente.getSquadra();
		
		if(squadra == null) {
			return "presidente/nessunaSquadra.html";
		} else {
			List<Tesseramento> tesseramentiSquadra = squadra.getTesseramenti();
			model.addAttribute("tesseramenti", tesseramentiSquadra);
			model.addAttribute("squadra", squadra);
			return "presidente/cancellaGiocatore.html";
		}
	}
	
	@PostMapping("/presidente/cancellaTesseramento/{idSquadra}") 
	public String formCancellaTesseramento(Model model, @RequestParam("idTesseramento") Long id, @PathVariable("idSquadra") Long idS) {
		Squadra squadraCorrente = this.squadraRepository.findById(idS).get();
		List<Tesseramento> tesseramentiSquadra = squadraCorrente.getTesseramenti();
		
		Tesseramento tesseramentoDaEliminare = this.tesseramentoRepository.findById(id).get();
		if(tesseramentiSquadra.contains(tesseramentoDaEliminare)) {
			tesseramentiSquadra.remove(tesseramentoDaEliminare);
			tesseramentoDaEliminare.setSquadra(null);
			tesseramentoDaEliminare.setGiocatore(null);
			this.tesseramentoRepository.delete(tesseramentoDaEliminare);
		}
		return "presidente/homePagePresidente.html";
	}
	
}
