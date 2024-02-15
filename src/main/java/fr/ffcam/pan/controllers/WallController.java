package fr.ffcam.pan.controllers;

import java.io.IOException;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import fr.ffcam.pan.models.walls.Wall;
import fr.ffcam.pan.models.walls.WallRepository;
import fr.ffcam.pan.models.walls.WallService;

@Secured("ROLE_ADMIN")
@Controller
public class WallController {

	
	@Autowired
	private WallRepository wallRepository;

	@Autowired
	private WallService wallService;
	
	@GetMapping("/walls")
	public String index(Model model) {
		var walls = wallRepository.findAll();
		
		model.addAttribute("walls", walls);
		
		return "walls/index";
	}
	
	@GetMapping("/walls/{id}")
	public String show(@PathVariable("id") UUID id, Model model) {
		
		var wall = wallRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		model.addAttribute("wall", wall);
		
		return "walls/show";
	}
	
	@GetMapping("/walls/new")
	public String createForm(Model model) {
		model.addAttribute("wall", new Wall());
		return "walls/new";
	}
	
	@PostMapping("/walls/new")
	public String create(@RequestParam("image") MultipartFile image, @RequestParam("name") String name) {
		
		try {
			var wall = wallService.storeImage(image.getInputStream(), name);
			
			return "redirect:/walls/" + wall.getId();
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/walls/edit")
	public String editForm() {
		
		return "walls/edit";
	}
	
	@PutMapping("/walls/edit")
	public String edit() {
		
		return "reditect:/walls/{id}";
	}
}
