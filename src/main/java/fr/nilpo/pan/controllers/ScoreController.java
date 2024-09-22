package fr.nilpo.pan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fr.nilpo.pan.models.users.AppUserScoreRepository;

@Controller
public class ScoreController {

	
	@Autowired
	private AppUserScoreRepository userScoreRepository;
	
	@GetMapping("/scores")
	public String index(Model model, Pageable pageable) {
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("score").descending());
		
		var scores = userScoreRepository.findAll(pageable);
		
		var rankOffset = pageable.getPageSize() * pageable.getPageNumber();
		
		model.addAttribute("scores", scores);
		model.addAttribute("rankOffset", rankOffset);
		
		return "scores/index";
	}
	
}
