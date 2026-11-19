package fr.nilpo.pan.models.boulders;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Optional;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PhotoService {

	private static final Logger log = LoggerFactory.getLogger(PhotoService.class);

	private static final Pattern ID = Pattern.compile("^[0-9a-f]{64}$");

	@Autowired
	private PhotoRepository photoRepository;

	/**
	 * Decodes, validates and re-encodes an uploaded photo (see ImageHelper),
	 * then stores it unless an identical one already exists.
	 *
	 * @throws IllegalArgumentException if the data URL isn't a valid image.
	 *         Thrown before anything is written, and callers turn it into a
	 *         form error: it must not mark their transaction rollback-only.
	 */
	@Transactional(noRollbackFor = IllegalArgumentException.class)
	public Photo save(String dataUrl) {
		var image = ImageHelper.parseDataUrl(dataUrl);
		var id = sha256(image.getData());

		photoRepository.insertIfAbsent(id, image.getData(), image.getWidth(), image.getHeight());

		return photoRepository.findById(id).orElseThrow();
	}

	/** Empty for anything but an existing photo id (request input). */
	public Optional<Photo> find(String id) {
		if (id == null || !ID.matcher(id).matches()) return Optional.empty();
		return photoRepository.findById(id);
	}

	public Optional<byte[]> findData(String id) {
		if (id == null || !ID.matcher(id).matches()) return Optional.empty();
		return photoRepository.findDataById(id);
	}

	/** Photos are shared: only delete one that nothing references anymore. */
	@Transactional
	public void deleteIfUnused(Photo photo) {
		if (photo != null) photoRepository.deleteIfUnused(photo.getId());
	}

	/**
	 * A photo is stored as soon as it's uploaded, even when the rest of the
	 * form is invalid (so the re-rendered form shows it by id instead of
	 * uploading it again). If the form is then abandoned, nothing references
	 * the photo: delete those after a day, which leaves plenty of time to fix
	 * and resubmit a form.
	 */
	static final int UNUSED_PHOTO_MAX_AGE_HOURS = 24;

	@Scheduled(initialDelay = 600_000, fixedDelay = 3_600_000)
	@Transactional
	public void deleteAbandoned() {
		int deleted = photoRepository.deleteUnusedOlderThan(UNUSED_PHOTO_MAX_AGE_HOURS);
		if (deleted > 0) log.info("deleted {} unused photo(s)", deleted);
	}

	private static String sha256(byte[] data) {
		try {
			return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(data));
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e); // every JVM ships SHA-256
		}
	}
}
