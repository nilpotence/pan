package fr.nilpo.pan.controllers;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import fr.nilpo.pan.models.boulders.PhotoService;

@Controller
public class PhotoController {

	@Autowired
	private PhotoService photoService;

	/**
	 * The id is the SHA-256 of the content, so a URL always serves the same
	 * bytes: cached for good, and downloaded once for all the boulders that
	 * share the photo. Photos are always re-encoded as JPEG (ImageHelper).
	 */
	@GetMapping("/photos/{id}")
	public ResponseEntity<byte[]> show(@PathVariable("id") String id) {
		var data = photoService
				.findData(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_JPEG)
				.cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic().immutable())
				.body(data);
	}
}
