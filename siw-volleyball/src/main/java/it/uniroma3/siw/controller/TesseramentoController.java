package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Tesseramento;
import it.uniroma3.siw.service.GiocatoreService;
import it.uniroma3.siw.service.TesseramentoService;

@Controller
public class TesseramentoController {
	
	
	/*@PostMapping("/tesseramenti") 
	public String NewGiocatore(@ModelAttribute("tesseramento") Tesseramento tesseramento, Model model) {
		this.tesseramentoService.save(tesseramento);
		return "formNewTesseramento.html";
	}*/
	
	
}
