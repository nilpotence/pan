package fr.nilpo.pan.controllers;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
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

import fr.nilpo.pan.config.AuthService;
import fr.nilpo.pan.models.boulders.Boulder;
import fr.nilpo.pan.models.boulders.BoulderIndexData;
import fr.nilpo.pan.models.boulders.BoulderRepository;
import fr.nilpo.pan.models.boulders.DefaultPhoto;
import fr.nilpo.pan.models.boulders.DefaultPhotoRepository;
import fr.nilpo.pan.models.boulders.ShowBoulderHelper;
import fr.nilpo.pan.models.boulders.Tick;
import fr.nilpo.pan.models.boulders.TickRepository;


@Controller
public class BoulderController {
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private BoulderRepository boulderRepository;
	
	@Autowired
	private DefaultPhotoRepository defaultPhotoRepository;
	
	@Autowired
	private TickRepository tickRepository;
	
	@GetMapping("/boulders")
	public String index(Model model, @SortDefault(sort = {"createdAt"}, direction = Direction.DESC) Pageable pageable) {
        var user = authService.getCurrentUser();

		Page<BoulderIndexData> boulders = boulderRepository.findAll(user, pageable);
		
		var currentSort = pageable.getSort().stream()
			.map(o -> o.getProperty() + "," + o.getDirection().toString())
			.collect(Collectors.toList());
		
		model.addAttribute("boulders", boulders);
		model.addAttribute("currentSort", currentSort);
		
		return "boulders/index";
	}
	
	@GetMapping("/boulders/{id}")
	public String show(
			@PathVariable("id") UUID id,
			@SortDefault(sort = {"createdAt"}, direction = Direction.DESC) Pageable pageable,
			Model model) {
		Boulder b = boulderRepository
				.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		
		if (authService.getCurrentUser() != null) {
			Tick t = tickRepository
				.findById(new Tick.TickId(authService.getCurrentUser().getId(), b.getId()))
				.orElse(new Tick());
			
			model.addAttribute("tick", t);
		}
		
		Page<Tick> ticks = tickRepository
				.findAllByBoulder(b, pageable);
		
		var bbox = ShowBoulderHelper.getBoulderBoundingBox(b);
		
		model.addAttribute("boulder", b);
		model.addAttribute("ticks", ticks);
		model.addAttribute("bbox", bbox);
		
		
		return "boulders/show";
	}
	
	@PutMapping("/boulders/{id}/obsolete")
	@Transactional
	public String obsolete(@PathVariable("id") Boulder boulder) {
		if (authService.getCurrentUser() == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}

		boulder.setObsoleteCount(boulder.getObsoleteCount() + 1);
		boulderRepository.save(boulder);
		
		return "redirect:/boulders";
	}
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/boulders/{id}/unobsolete")
	@Transactional
	public String unobsolete(@PathVariable("id") Boulder boulder) {
		boulder.setObsoleteCount(0);
		boulderRepository.save(boulder);
		
		return "redirect:/boulders/{id}";
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

		var defaultPhoto = new DefaultPhoto();

		defaultPhoto.setWidth(boulder.getCustomPhotoWidth());
		defaultPhoto.setHeight(boulder.getCustomPhotoHeight());
		defaultPhoto.setRawData(boulder.getRawCustomPhoto());
		
		model.addAttribute("defaultPhoto", defaultPhoto);
		model.addAttribute("boulder", boulder);
		
		return "boulders/edit";
	}
	
	@PutMapping(path = "/boulders/{id}/edit")
	@Transactional
	public String edit(
			@PathVariable("id") Boulder boulder, 
			@ModelAttribute("boulder") @Valid Boulder updatedBoulder,
			BindingResult results,
			Model model) {
		
		if (results.hasErrors()) {
			var defaultPhoto = new DefaultPhoto();

			defaultPhoto.setWidth(boulder.getCustomPhotoWidth());
			defaultPhoto.setHeight(boulder.getCustomPhotoHeight());
			defaultPhoto.setRawData(boulder.getRawCustomPhoto());
			
			model.addAttribute("defaultPhoto", defaultPhoto);
			
			return "boulders/edit";
		}
		
		if (!authService.canUpdate(boulder)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		
		boulder.setName(updatedBoulder.getName());
		boulder.setEstimatedGrade(updatedBoulder.getEstimatedGrade());
		boulder.setComment(updatedBoulder.getComment());
		boulder.setHolds(updatedBoulder.getHolds());
		boulder.setRawCustomPhoto(updatedBoulder.getRawCustomPhoto());
		boulder.setCustomPhotoWidth(updatedBoulder.getCustomPhotoWidth());
		boulder.setCustomPhotoHeight(updatedBoulder.getCustomPhotoHeight());
		
		boulder.setLastUpdatedAt(LocalDateTime.now());
		boulder.setLastUpdatedBy(authService.getCurrentUser());
		
		boulderRepository.save(boulder);
		
		return "redirect:/boulders/{id}";
	}
	
	@GetMapping("/boulders/new")
	public String createForm(Model model) {		
		var defaultPhoto = defaultPhotoRepository
				.findFirstByOrderByCreatedAtDesc()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
		
		model.addAttribute("defaultPhoto", defaultPhoto);
		model.addAttribute("boulder", new Boulder());
		return "boulders/new";
	}
	
	@PostMapping("/boulders/new")
	@Transactional
	public String create(
			@ModelAttribute("boulder") @Valid Boulder boulder,
			BindingResult bindingResult,
			Model model) {
		
		if (bindingResult.hasErrors()) {
			var defaultPhoto = new DefaultPhoto();

			defaultPhoto.setWidth(boulder.getCustomPhotoWidth());
			defaultPhoto.setHeight(boulder.getCustomPhotoHeight());
			defaultPhoto.setRawData(boulder.getRawCustomPhoto());
			
			model.addAttribute("defaultPhoto", defaultPhoto);
			
			return "boulders/new";
		}
		
		boulder.setCreatedBy(authService.getCurrentUser());
		boulder.setCreatedAt(LocalDateTime.now());
		
		boulderRepository.save(boulder);
		
		return "redirect:" + boulder.getId();
	}
	
	@PostMapping("/boulders/{id}/tick")
	@Transactional
	public String tickBoulder(
		@PathVariable("id") UUID boulderId,
		String comment,
		String estimatedGrade,
		boolean ticked,
		Model model
	){
		var tick = tickRepository
				.findById(new Tick.TickId(authService.getCurrentUser().getId(), boulderId))
				.orElse(new Tick());
		
		if (tick.getAppUser() == null) {
			if (ticked) {
				tick.setAppUser(authService.getCurrentUser());
				tick.setBoulder(boulderRepository.getReferenceById(boulderId));
				tick.setCreatedAt(LocalDateTime.now());
				tick.setComment(comment);
				tick.setEstimatedGrade(estimatedGrade);

				tickRepository.save(tick);
			}
		} else {
			if (ticked) {
				tick.setComment(comment);
				tick.setEstimatedGrade(estimatedGrade);
		
				tickRepository.save(tick);
			} else {
				tickRepository.delete(tick);
			}
		}
		
		return "redirect:/boulders/" + boulderId;
	}
}
