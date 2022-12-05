package fr.ffcam.pan.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fr.ffcam.pan.models.boulders.Boulder;
import fr.ffcam.pan.models.boulders.BoulderRepository;

@Controller
public class BoulderController {
	
	@Autowired
	private BoulderRepository boulderRepository;
	
	@GetMapping("/boulders")
	public String index(Model model) {
		List<Boulder> boulders = boulderRepository.findAll();
		
		model.addAttribute("boulders", boulders);
		
		return "boulders/index";
	}
	
	@GetMapping("/boulders/{id}")
	public String show(@PathVariable("id") UUID id, Model model) {
		Boulder b = boulderRepository.findById(id).orElseThrow();
		
		model.addAttribute("boulder", b);
		
		return "boulders/show";
	}
	
	@GetMapping("/boulders/new")
	public String createForm() {
		return "boulders/new";
	}
	
	@PostMapping("/boulders/new")
	public String create(String name, String estimatedGrade, String holds) {
		
		Boulder boulder = new Boulder();
		boulder.setName(name);
		boulder.setEstimatedGrade(estimatedGrade);
		boulder.setHolds(holds);
		
		boulderRepository.save(boulder);
		
		return "redirect:" + boulder.getId();
	}
}
