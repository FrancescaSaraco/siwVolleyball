package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Tesseramento;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.GiocatoreRepository;
import it.uniroma3.siw.repository.PresidenteRepository;
import it.uniroma3.siw.repository.SquadraRepository;
import it.uniroma3.siw.repository.TesseramentoRepository;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.TesseramentoService;
import it.uniroma3.siw.service.UtenteService;
import it.uniroma3.siw.validator.CredenzialiValidator;
import it.uniroma3.siw.validator.UtenteValidator;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {
	@Autowired
	private CredenzialiService credentialsService;

	@Autowired
	private UtenteService utenteService;

	@Autowired
	public UtenteValidator utenteValidator;

	@Autowired
	public CredenzialiValidator credenzialiValidator;

	@Autowired SquadraRepository squadraRepository;

	@Autowired GiocatoreRepository giocatoreRepository;

	@Autowired PresidenteRepository presidenteRepository;

	@Autowired TesseramentoRepository tesseramentoRepository;
	
	@Autowired TesseramentoService tesseramentoService;



	@GetMapping(value = "/register") 
	public String showRegisterForm (Model model) {
		model.addAttribute("utente", new Utente());
		model.addAttribute("credenziali", new Credenziali());
		return "formRegisterUtente.html";
	}

	@GetMapping(value = "/login") 
	public String showLoginForm (Model model) {
		return "formLogin.html";
	}

	@GetMapping(value = "/") 
	public String homePage(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			model.addAttribute("squadre", this.squadraRepository.findAll());
			return "formLogin.html";
		}
		else {		
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credenziali credenziali = credentialsService.getCredentials(userDetails.getUsername());
			if (credenziali.getRole().equals(Credenziali.ADMIN_ROLE)) {
				model.addAttribute("utente", credenziali);
				return "admin/homePageAdmin.html";
			}
			else if (credenziali.getRole().equals(Credenziali.PRESIDENTE_ROLE)){

				Utente utente = credenziali.getUser();
				
				Presidente presidente = this.presidenteRepository.findByUtente(utente);


				//solo se ha la squadra fai questo
				if(presidente.getSquadra() != null) {
					Squadra squadra = presidente.getSquadra();

					List<Tesseramento> listaTesseramenti = (List<Tesseramento>) this.tesseramentoRepository.findAll();

					List<Tesseramento> listaTesseramentiSquadra = new ArrayList<>();

					//lista di tutti i giocatori
					List<Giocatore> listaGiocatoriNonInSquadra = (List<Giocatore>) this.giocatoreRepository.findAll();

					//la lista dei tesseramenti totali non deve essere vuota
					if(!listaTesseramenti.isEmpty()) {	
						//prendo solo quelli che sono di questa squadra
						for(Tesseramento t: listaTesseramenti) {
							if(t.getSquadra().equals(squadra)) {
								listaTesseramentiSquadra.add(t);
							}
						}


						//controllo che non sia vuota	
						if(!listaTesseramentiSquadra.isEmpty())
							//mi servono anche i giocatori che non sono in questa squadra
							for(Tesseramento t : listaTesseramentiSquadra) {
								//quindi elimino i giocatori già tesserati nella mia squadra da quelli totali
								listaGiocatoriNonInSquadra.remove(t.getGiocatore());
							}


						

					}
					
					model.addAttribute("listaTesseramenti", listaTesseramentiSquadra);
					model.addAttribute("squadra", squadra); 
					model.addAttribute("giocatoriNonInSquadra", listaGiocatoriNonInSquadra);
				}

				model.addAttribute("presidente", presidente);
				model.addAttribute("utente", credenziali);
				return "presidente/homePagePresidente.html";
			}
			else{
				model.addAttribute("squadre", this.squadraRepository.findAll());
				model.addAttribute("utente", credenziali);
				return "homePageUtente.html";
			}				
		}
	}

	@GetMapping(value = "/success")
	public String defaultAfterLogin(Model model) {

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credenziali credenziali= credentialsService.getCredentials(userDetails.getUsername());
		if (credenziali.getRole().equals(Credenziali.ADMIN_ROLE)) {
			model.addAttribute("utente", credenziali);
			return "admin/homePageAdmin.html";
		}
		else if (credenziali.getRole().equals(Credenziali.PRESIDENTE_ROLE)){

			//devo trovar il presidente per nome e cognome
			Utente utente = credenziali.getUser();
			
			Presidente presidente = presidenteRepository.findByUtente(utente);
			
			if(presidente.getSquadra() != null) {
				Squadra squadra = presidente.getSquadra();
				model.addAttribute("squadra", squadra);
			}
			
			//model.addAttribute("giocatoriNonInSquadra", this.tesseramentoService.findRimasti());
			model.addAttribute("utente", credenziali);
			model.addAttribute("presidente",presidente); 
			return "presidente/homePagePresidente.html";
		}

		model.addAttribute("squadre", this.squadraRepository.findAll());
		model.addAttribute("utente", credenziali);
		return "homePageUtente.html";
	}

	@PostMapping(value = {"/register"})
	public String registerUser(@Valid @ModelAttribute("utente") Utente utente,
			BindingResult userBindingResult, @Valid
			@ModelAttribute("credenziali") Credenziali credenziali,
			BindingResult credentialsBindingResult,
			Model model) {

		this.utenteValidator.validate(utente, userBindingResult);
		this.credenzialiValidator.validate(credenziali, credentialsBindingResult);

		// se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
		if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			utenteService.saveUser(utente);
			credenziali.setUser(utente); 
			credentialsService.saveCredentials(credenziali);
			if(credenziali.getRole().equals(Credenziali.PRESIDENTE_ROLE)) {
				model.addAttribute("presidente", utente);
				return "presidente/registrationSuccessfulPresidente.html";
			}
			model.addAttribute("cred", credenziali);
			model.addAttribute("utente", utente);
			return "registrationSuccessful.html";
		}

		model.addAttribute("credenziali", credenziali);
		model.addAttribute("utente", utente);
		return "formRegisterUtente.html";
	}

}