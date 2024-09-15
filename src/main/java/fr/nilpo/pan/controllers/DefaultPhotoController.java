package fr.nilpo.pan.controllers;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import fr.nilpo.pan.config.AuthService;
import fr.nilpo.pan.models.boulders.DefaultPhoto;
import fr.nilpo.pan.models.boulders.DefaultPhotoRepository;

@Controller
@Secured("ROLE_ADMIN")
public class DefaultPhotoController {
	
	@Autowired
	private DefaultPhotoRepository defaultPhotoRepository;
	
	@Autowired
	private AuthService authService;

	@GetMapping("/default-photos")
	public String index(
			Model model,
			@SortDefault(sort = {"createdAt"}, direction = Direction.DESC) Pageable pageable) {

		Page<DefaultPhoto> photos = defaultPhotoRepository.findAll(pageable);
		
		var currentSort = pageable.getSort().stream()
			.map(o -> o.getProperty() + "," + o.getDirection().toString())
			.collect(Collectors.toList());
		
		model.addAttribute("photos", photos);
		model.addAttribute("currentSort", currentSort);
		
		return "boulders/defaultphotos/index";
	}
	
	@GetMapping("/default-photos/{id}")
	public String show(@PathVariable("id") UUID id, Model model) {
		DefaultPhoto photo = defaultPhotoRepository 
				.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		
		model.addAttribute("photo", photo);
		
		return "boulders/defaultphotos/show";
	}
	
	@DeleteMapping("/default-photos/{id}")
	@Transactional
	public String delete(@PathVariable("id") DefaultPhoto boulder) {
		defaultPhotoRepository.delete(boulder);
		return "redirect:/default-photos";
	}
	
	@GetMapping("/default-photos/new")
	public String createForm(Model model) {		
		model.addAttribute("photo", new DefaultPhoto());
		return "boulders/defaultphotos/new";
	}
	
	@PostMapping("/default-photos/new")
	@Transactional
	public String create(@ModelAttribute("photo") DefaultPhoto photo) {
		
		photo.setCreatedBy(authService.getCurrentUser());
		photo.setCreatedAt(LocalDateTime.now());
		
		defaultPhotoRepository.save(photo);
		
		return "redirect:" + photo.getId();
	}
}
