package fr.nilpo.pan.controllers;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientErrorController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@GetMapping(path = "/api/errors", produces = "application/json")
	public String logError(String message) {

		try {
			var decoded = new String(Base64.getDecoder().decode(message), StandardCharsets.UTF_8);
			logger.info(decoded);
		} catch (Exception e) {
			logger.info(message);
		}
		
		return "\"OK\"";
	}
	
}
