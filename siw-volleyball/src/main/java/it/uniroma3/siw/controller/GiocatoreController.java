package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.GiocatoreService;
import it.uniroma3.siw.validator.GiocatoreValidator;
import jakarta.validation.Valid;

@Controller
public class GiocatoreController {

	@Autowired
	GiocatoreService giocatoreService;
	@Autowired 
	GiocatoreValidator giocatoreValidator;
	@Autowired
	CredenzialiService credenzialiService;
	
	/*@GetMapping("/giocatori")
	public String listaGiocatori(Model model) {
		model.addAttribute("giocatori", this.giocatoreService.findAll());
		return "giocatori.html";
	}*/
	
	@PostMapping(value = "/admin/giocatoriNuovi") 
	public String NewGiocatore(@Valid @ModelAttribute Giocatore giocatore, BindingResult result, Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credenziali credenziali = credenzialiService.getCredentials(userDetails.getUsername());
		
		this.giocatoreValidator.validate(giocatore, result);
		
		if(!result.hasErrors()) {
			this.giocatoreService.save(giocatore);
			model.addAttribute("utente", credenziali);
			return "admin/homePageAdmin.html";
		}
		else {
			model.addAttribute("giocatore", giocatore);
			return "admin/formNewGiocatore.html";
		}
	}
	
	@GetMapping(value = "/admin/formNewGiocatore") 
	public String showFormNewGiocatore (Model model) {
		model.addAttribute("giocatore", new Giocatore());
		return "admin/formNewGiocatore.html";
	}
}
