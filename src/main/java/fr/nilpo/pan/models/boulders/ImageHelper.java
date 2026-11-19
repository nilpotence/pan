package fr.nilpo.pan.models.boulders;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ImageHelper {

	// The browser resizes every photo to 1600px wide before upload
	// (take_photo.js), so a tall portrait shot is about 1600x2850. Anything
	// beyond these limits is rejected from the header alone, before the
	// pixels are decoded, so a small file declaring huge dimensions
	// (decompression bomb) can't make us allocate gigabytes.
	static final int MAX_DATA_URL_LENGTH = 2_000_000;
	static final int MAX_SIDE = 4000;
	static final long MAX_PIXELS = 8_000_000L;

	private static final Pattern DATA_URL_PREFIX = Pattern.compile("^data:image/(jpeg|png);base64,");
	private static final Set<String> ALLOWED_FORMATS = Set.of("jpeg", "png");

	public static ImageDetails parseDataUrl(String dataUrl) {
		if (dataUrl == null || dataUrl.length() > MAX_DATA_URL_LENGTH) {
			throw new IllegalArgumentException("Image too large or missing");
		}

		var prefix = DATA_URL_PREFIX.matcher(dataUrl);
		if (!prefix.lookingAt()) {
			throw new IllegalArgumentException("Unsupported data URL");
		}

		byte[] rawData;
		try {
			rawData = Base64.getDecoder().decode(dataUrl.substring(prefix.end()).trim());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid base64 image data", e);
		}

		try (var input = ImageIO.createImageInputStream(new ByteArrayInputStream(rawData))) {
			if (input == null) {
				throw new IllegalArgumentException("Not a recognized image");
			}
			var readers = ImageIO.getImageReaders(input);
			if (!readers.hasNext()) {
				throw new IllegalArgumentException("Not a recognized image");
			}

			var reader = readers.next();
			try {
				if (!ALLOWED_FORMATS.contains(reader.getFormatName().toLowerCase(Locale.ROOT))) {
					throw new IllegalArgumentException("Unsupported image format");
				}

				reader.setInput(input, true, true);

				// Header only: no pixel buffer is allocated yet.
				int width = reader.getWidth(0);
				int height = reader.getHeight(0);
				if (width <= 0 || height <= 0 || width > MAX_SIDE || height > MAX_SIDE
						|| (long) width * height > MAX_PIXELS) {
					throw new IllegalArgumentException("Image dimensions out of bounds: " + width + "x" + height);
				}

				var img = toRgb(reader.read(0));

				return new ImageDetails(saveImage(img), img.getWidth(), img.getHeight());
			} finally {
				reader.dispose();
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("Invalid image data", e);
		}
	}

	// The JPEG writer can't encode an alpha channel (e.g. a PNG upload).
	private static BufferedImage toRgb(BufferedImage img) {
		if (!img.getColorModel().hasAlpha()) return img;

		var rgb = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		var g = rgb.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		g.drawImage(img, 0, 0, null);
		g.dispose();
		return rgb;
	}

	private static byte[] saveImage(BufferedImage image) throws IOException {
		var outputData = new ByteArrayOutputStream();

		var writers = ImageIO.getImageWritersByFormatName("jpg");
		var writer = writers.next();
		writer.setOutput(ImageIO.createImageOutputStream(outputData));
		
		var params = writer.getDefaultWriteParam();
		params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		params.setCompressionQuality(0.5f);
		
		writer.write(null, new IIOImage(image, null, null), params);
		
		return outputData.toByteArray();
	}
	
	
	@RequiredArgsConstructor
	@Getter
	public static class ImageDetails {
		private final byte[] data;
		private final int width;
		private final int height;
	}
}
