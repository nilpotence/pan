package fr.nilpo.pan.controllers;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;

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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import fr.nilpo.pan.config.AuthService;
import fr.nilpo.pan.models.boulders.Boulder;
import fr.nilpo.pan.models.boulders.BoulderIndexData;
import fr.nilpo.pan.models.boulders.BoulderRepository;
import fr.nilpo.pan.models.boulders.DefaultPhotoRepository;
import fr.nilpo.pan.models.boulders.Photo;
import fr.nilpo.pan.models.boulders.PhotoService;
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
	
	@Autowired
	private PhotoService photoService;

	private static final Pattern GRADE = Pattern.compile("^[3-8][a-c]\\+{0,1}$");
	static final int MAX_TICK_COMMENT_LENGTH = 1000;

	/** 404 if the boulder doesn't exist, 403 if the current user may not modify it. */
	private Boulder findUpdatable(UUID id) {
		var boulder = boulderRepository
				.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if (!authService.canUpdate(boulder)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		return boulder;
	}

	/**
	 * Only user-editable fields are bound onto a submitted Boulder. The photo
	 * comes separately (photoId / customPhoto, see resolvePhoto).
	 */
	@InitBinder("boulder")
	public void initBoulderBinder(WebDataBinder binder) {
		binder.setAllowedFields("name", "comment", "estimatedGrade", "holds");
	}

	/**
	 * The photo of a submitted boulder form: a new photo taken by the user
	 * (customPhoto, a data URL), else the photo the form was showing
	 * (photoId: the default photo, or the boulder's current photo when
	 * editing). Only a new photo is uploaded; it is stored deduplicated.
	 *
	 * @return null, with an error on the "photo" field, if neither is valid
	 */
	private Photo resolvePhoto(String photoId, String customPhoto, BindingResult bindingResult) {
		if (customPhoto != null && !customPhoto.isBlank()) {
			try {
				return photoService.save(customPhoto);
			} catch (IllegalArgumentException e) {
				bindingResult.rejectValue("photo", "invalid", "Cette photo n'est pas valide, veuillez en prendre une autre !");
				return null;
			} catch (PhotoService.BusyException e) {
				bindingResult.rejectValue("photo", "busy", "Le serveur traite déjà d'autres photos, veuillez réessayer dans un instant !");
				return null;
			}
		}
		
		var photo = photoService.find(photoId);
		if (photo.isEmpty()) {
			bindingResult.rejectValue("photo", "missing", "Vous devez choisir une photo pour votre bloc !");
			return null;
		}
		return photo.get();
	}

	private Photo latestDefaultPhoto() {
		return defaultPhotoRepository
				.findFirstByOrderByCreatedAtDesc()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR))
				.getPhoto();
	}
	
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

        var gradeStats = tickRepository.getGradeStatsByBoulder(b);

        long gradeStatsCount = 0;

        for (var gs : gradeStats) {
            gradeStatsCount += gs.getCount();
        }
		
		var bbox = ShowBoulderHelper.getBoulderBoundingBox(b);
		
		model.addAttribute("boulder", b);
		model.addAttribute("ticks", ticks);
		model.addAttribute("bbox", bbox);
        model.addAttribute("gradeStatsCount", gradeStatsCount);
        model.addAttribute("gradeStats", gradeStats);
		
		
		return "boulders/show";
	}
	
	@PutMapping("/boulders/{id}/obsolete")
	@Transactional
	public String obsolete(@PathVariable("id") UUID boulderId) {
		var user = authService.getCurrentUser();
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		if (!boulderRepository.existsById(boulderId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		// One vote per user: the count only moves when a new vote row is inserted.
		if (boulderRepository.addObsoleteVote(user.getId(), boulderId) == 1) {
			boulderRepository.incrementObsoleteCount(boulderId);
		}
		
		return "redirect:/boulders";
	}
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/boulders/{id}/unobsolete")
	@Transactional
	public String unobsolete(@PathVariable("id") UUID boulderId) {
		if (!boulderRepository.existsById(boulderId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		boulderRepository.clearObsoleteVotes(boulderId);
		boulderRepository.resetObsoleteCount(boulderId);
		
		return "redirect:/boulders/{id}";
	}
	
	@DeleteMapping("/boulders/{id}")
	@Transactional
	public String delete(@PathVariable("id") UUID id) {
		var boulder = findUpdatable(id);
		
		boulderRepository.deleteBoulder(boulder.getId());
		photoService.deleteIfUnused(boulder.getPhoto());
		
		return "redirect:/boulders";
	}
	
	@GetMapping(path = "/boulders/{id}/edit")
	public String editForm(@PathVariable("id") UUID id, Model model) {
		var boulder = findUpdatable(id);
		
		model.addAttribute("formPhoto", boulder.getPhoto());
		model.addAttribute("boulder", boulder);
		
		return "boulders/edit";
	}
	
	@PutMapping(path = "/boulders/{id}/edit")
	@Transactional
	public String edit(
			@PathVariable("id") UUID id, 
			@ModelAttribute("boulder") @Valid Boulder updatedBoulder,
			BindingResult results,
			@RequestParam(name = "photoId", required = false) String photoId,
			@RequestParam(name = "customPhoto", required = false) String customPhoto,
			Model model) {
		
		// Authorization first: a non-owner must not get the edit form back
		// (nor upload a photo) just by submitting invalid data.
		var boulder = findUpdatable(id);
		
		var photo = resolvePhoto(photoId, customPhoto, results);
		
		if (results.hasErrors()) {
			model.addAttribute("formPhoto", photo != null ? photo : boulder.getPhoto());
			
			return "boulders/edit";
		}
		
		var previousPhoto = boulder.getPhoto();
		
		boulder.setName(updatedBoulder.getName());
		boulder.setEstimatedGrade(updatedBoulder.getEstimatedGrade());
		boulder.setComment(updatedBoulder.getComment());
		boulder.setHolds(updatedBoulder.getHolds());
		boulder.setPhoto(photo);
		
		boulder.setLastUpdatedAt(LocalDateTime.now());
		boulder.setLastUpdatedBy(authService.getCurrentUser());
		
		boulderRepository.save(boulder);
		
		if (!previousPhoto.getId().equals(photo.getId())) {
			photoService.deleteIfUnused(previousPhoto);
		}
		
		return "redirect:/boulders/{id}";
	}
	
	@GetMapping("/boulders/new")
	public String createForm(Model model) {		
		model.addAttribute("formPhoto", latestDefaultPhoto());
		model.addAttribute("boulder", new Boulder());
		return "boulders/new";
	}
	
	@PostMapping("/boulders/new")
	@Transactional
	public String create(
			@ModelAttribute("boulder") @Valid Boulder boulder,
			BindingResult bindingResult,
			@RequestParam(name = "photoId", required = false) String photoId,
			@RequestParam(name = "customPhoto", required = false) String customPhoto,
			Model model) {
		
		var photo = resolvePhoto(photoId, customPhoto, bindingResult);
		
		if (bindingResult.hasErrors()) {
			// A valid new photo is already stored: shown again by id, so
			// the form doesn't upload it a second time.
			model.addAttribute("formPhoto", photo != null ? photo : latestDefaultPhoto());
			
			return "boulders/new";
		}
		
		boulder.setPhoto(photo);
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
		if (!boulderRepository.existsById(boulderId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		// The form only offers valid grades (or none) and a short comment:
		// anything else is a forged request.
		estimatedGrade = (estimatedGrade == null || estimatedGrade.isBlank()) ? null : estimatedGrade;
		if (estimatedGrade != null && !GRADE.matcher(estimatedGrade).matches()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid grade");
		}
		comment = (comment == null || comment.isBlank()) ? null : comment.strip();
		if (comment != null && comment.length() > MAX_TICK_COMMENT_LENGTH) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment too long");
		}

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
