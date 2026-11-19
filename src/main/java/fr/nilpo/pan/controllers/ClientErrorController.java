package fr.nilpo.pan.controllers;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientErrorController {

	static final int MAX_LOGGED_LENGTH = 1000;
	static final int MAX_MESSAGES_PER_MINUTE = 60;

	@Autowired
	private fr.nilpo.pan.config.RateLimiter rateLimiter;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Unauthenticated endpoint fed by send_client_error.js: everything logged
	 * here is attacker-controlled, so it is truncated and control characters
	 * are escaped (no forged log lines via CR/LF).
	 */
	@GetMapping(path = "/api/errors", produces = "application/json")
	public ResponseEntity<String> logError(String message, jakarta.servlet.http.HttpServletRequest request) {
		if (message == null || message.isEmpty()) {
			return ResponseEntity.ok("\"OK\"");
		}

		if (!rateLimiter.tryAcquire("client-errors|" + request.getRemoteAddr(), MAX_MESSAGES_PER_MINUTE, Duration.ofMinutes(1))) {
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("\"Too many requests\"");
		}

		String decoded;
		try {
			decoded = new String(Base64.getDecoder().decode(message), StandardCharsets.UTF_8);
		} catch (IllegalArgumentException e) {
			decoded = message;
		}

		logger.info("client error: {}", sanitize(decoded));

		return ResponseEntity.ok("\"OK\"");
	}

	static String sanitize(String s) {
		boolean truncated = s.length() > MAX_LOGGED_LENGTH;
		if (truncated) {
			s = s.substring(0, MAX_LOGGED_LENGTH);
		}

		var sb = new StringBuilder(s.length() + 16);
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
				case '\n': sb.append("\\n"); break;
				case '\r': sb.append("\\r"); break;
				case '\t': sb.append("\\t"); break;
				default:
					if (Character.isISOControl(c) || c == '\u2028' || c == '\u2029') {
						sb.append(String.format("\\u%04x", (int) c));
					} else {
						sb.append(c);
					}
			}
		}

		if (truncated) {
			sb.append("...[truncated]");
		}
		return sb.toString();
	}
}
