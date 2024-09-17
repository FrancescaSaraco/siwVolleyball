package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.service.PresidenteService;
import it.uniroma3.siw.service.SquadraService;
import it.uniroma3.siw.validator.SquadraValidator;
import jakarta.validation.Valid;

@Controller
public class SquadraController {
	
	
	@Autowired
	SquadraService squadraService;
	
	@Autowired
	PresidenteService presidenteService; 
	@Autowired
	SquadraValidator squadraValidator;
	
	// metodo che ritorna un template per visualizzare una lista di squadre al fine di modificarne delle caratteristiche da parte dell'admin
	@GetMapping("/admin/squadre")
	public String listaSquadre(Model model) {
		model.addAttribute("squadre", this.squadraService.findAll());
		return "admin/manageSquadre.html";
	}
	
	@PostMapping("/admin/squadre") 
	public String NewSquadra(@Valid @ModelAttribute("squadra") Squadra squadra, BindingResult result, Model model, @ModelAttribute("codiceFiscale") String codiceFiscale) {
		
		this.squadraValidator.validate(squadra, result);
		
		Presidente nuovo = this.presidenteService.findByCodiceFiscale(codiceFiscale);
		if(!result.hasErrors()){
			if(nuovo != null) {
				nuovo.setSquadra(squadra);
				squadra.setPresidente(nuovo);
		}
		this.squadraService.save(squadra);
		return "admin/homePageAdmin.html";
		}
		model.addAttribute("squadra", squadra);
		model.addAttribute("presidenti", this.presidenteService.findRimasti());
		return "admin/formNewSquadra.html";
	}
	
	@GetMapping(value = "/admin/formNewSquadra") 
	public String showFormNewSquadra (Model model) {
		model.addAttribute("squadra", new Squadra());
		model.addAttribute("presidenti", this.presidenteService.findRimasti());
		return "admin/formNewSquadra.html";
	}
	
	@GetMapping("/admin/formAggiornaSquadra/{id}")
	public String showFormAggiornamento(@PathVariable("id") Long id, Model model) {
		Squadra squadra = this.squadraService.findById(id).get();
		model.addAttribute("squadra", squadra);
		model.addAttribute("rimasti", this.presidenteService.findRimasti());
		return "admin/formAggiornaSquadra.html";
	}
	
	@PostMapping("/admin/modificaSquadra/{id}")
	public String modificaSquadra(@PathVariable("id") Long id,@ModelAttribute("squadra") Squadra squadra, @ModelAttribute("codiceFiscale") String codiceFiscale, Model model) {
		Squadra squadraVecchia = this.squadraService.findById(id).get();
		Presidente nuovo = this.presidenteService.findByCodiceFiscale(codiceFiscale);
		Presidente vecchio = squadraVecchia.getPresidente();
		
		/*if((nuovo != null) && !nuovo.equals(squadraVecchia.getPresidente())) {
			if(vecchio != null) {
				vecchio.setSquadra(null);
			}
			squadraVecchia.setPresidente(nuovo);
			nuovo.setSquadra(squadraVecchia);
			
		}*/
		
		// devo aggiornare il presidente 
		// se quello vecchio non c'era allora metti quello nuovo direttamente
		if(vecchio == null) {
			squadraVecchia.setPresidente(nuovo);
			nuovo.setSquadra(squadraVecchia);
		} else {
			// altrimenti metti il riferimento di presiente vecchio a squadra vecchia null
			vecchio.setSquadra(null);
			squadraVecchia.setPresidente(nuovo);
			nuovo.setSquadra(squadraVecchia);
		}
		
		
		
		if(!squadra.getNome().equals(squadraVecchia.getNome())) {
			squadraVecchia.setNome(squadra.getNome());
		}
		
		if((squadra.getAnnoFondazione() != null) && !squadra.getAnnoFondazione().equals(squadraVecchia.getAnnoFondazione())) {
			squadraVecchia.setAnnoFondazione(squadra.getAnnoFondazione());
		}
		
		if(!(squadra.getIndirizzo().equals(squadraVecchia.getIndirizzo()))) {
			squadraVecchia.setIndirizzo(squadra.getIndirizzo());
		}
		
		
		
		this.squadraService.save(squadraVecchia);
		return "admin/homePageAdmin.html";
	}
	
}
