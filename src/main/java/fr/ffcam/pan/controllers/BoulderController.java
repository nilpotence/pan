package fr.ffcam.pan.controllers;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fr.ffcam.pan.models.boulders.Boulder;
import fr.ffcam.pan.models.boulders.BoulderRepository;
import fr.ffcam.pan.models.boulders.ShowBoulderHelper;

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
		
		var bbox = ShowBoulderHelper.getBoulderBoundingBox(b);
		
		model.addAttribute("boulder", b);
		model.addAttribute("bbox", bbox);
		
		
		return "boulders/show";
	}
	
	@GetMapping("/boulders/new")
	public String createForm(Model model) {
		model.addAttribute("boulder", new Boulder());
		return "boulders/new";
	}
	
	@PostMapping("/boulders/new")
	public String create(
			@ModelAttribute("boulder") @Valid Boulder boulder,
			BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			System.out.println("errors");
			for (var e : bindingResult.getAllErrors()) {
				System.out.println(e);
			}
			System.out.println(boulder.getEstimatedGrade());
			return "boulders/new";
		}
		
		boulderRepository.save(boulder);
		
		return "redirect:" + boulder.getId();
	}
}
