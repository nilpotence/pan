package fr.ffcam.pan.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.server.ResponseStatusException;

import fr.ffcam.pan.config.AuthService;
import fr.ffcam.pan.models.boulders.Boulder;
import fr.ffcam.pan.models.boulders.BoulderRepository;
import fr.ffcam.pan.models.boulders.ShowBoulderHelper;


@Controller
public class BoulderController {
	
	@Autowired
	private AuthService authService;
	
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
		Boulder b = boulderRepository
				.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		
		var bbox = ShowBoulderHelper.getBoulderBoundingBox(b);
		
		model.addAttribute("boulder", b);
		model.addAttribute("bbox", bbox);
		
		
		return "boulders/show";
	}
	
	@DeleteMapping("/boulders/{id}")
	@Transactional
	public String delete(@PathVariable("id") Boulder boulder) {
		if (!authService.canUpdate(boulder)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		
		boulderRepository.delete(boulder);
		
		return "redirect:/boulders";
	}
	
	@GetMapping(path = "/boulders/{id}/edit")
	public String editForm(@PathVariable("id") Boulder boulder, Model model) {
		if (!authService.canUpdate(boulder)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		
		model.addAttribute("boulder", boulder);
		
		return "boulders/edit";
	}
	
	@PutMapping(path = "/boulders/{id}/edit")
	@Transactional
	public String edit(
			@PathVariable("id") Boulder boulder, 
			@ModelAttribute("boulder") @Valid Boulder updatedBoulder,
			BindingResult results) {
		
		if (results.hasErrors()) {
			return "boulders/edit";
		}
		
		if (!authService.canUpdate(boulder)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		
		boulder.setName(updatedBoulder.getName());
		boulder.setEstimatedGrade(updatedBoulder.getEstimatedGrade());
		boulder.setHolds(updatedBoulder.getHolds());
		
		boulder.setLastUpdatedAt(LocalDateTime.now());
		boulder.setLastUpdatedBy(authService.getCurrentUser());
		
		boulderRepository.save(boulder);
		
		return "redirect:/boulders/{id}";
	}
	
	@GetMapping("/boulders/new")
	public String createForm(Model model) {
		model.addAttribute("boulder", new Boulder());
		return "boulders/new";
	}
	
	@PostMapping("/boulders/new")
	@Transactional
	public String create(
			@ModelAttribute("boulder") @Valid Boulder boulder,
			BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return "boulders/new";
		}
		
		boulder.setCreatedBy(authService.getCurrentUser());
		boulder.setCreatedAt(LocalDateTime.now());
		
		boulderRepository.save(boulder);
		
		return "redirect:" + boulder.getId();
	}
}
