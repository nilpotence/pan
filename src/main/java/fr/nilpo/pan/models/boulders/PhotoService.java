package fr.nilpo.pan.models.boulders;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PhotoService {

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

	private static String sha256(byte[] data) {
		try {
			return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(data));
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e); // every JVM ships SHA-256
		}
	}
}
