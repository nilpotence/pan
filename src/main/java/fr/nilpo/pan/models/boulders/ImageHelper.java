package fr.nilpo.pan.models.boulders;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ImageHelper {
	
	public static ImageDetails parseDataUrl(String dataUrl) {
		int dataStartIndex = dataUrl.indexOf(',') + 1;
		var data = dataUrl.substring(dataStartIndex);
		
		
		try {
			var rawData = Base64.getDecoder().decode(data.trim());

			var img = ImageIO.read(new ByteArrayInputStream(rawData));
			
			var width = img.getWidth();
			var height = img.getHeight();
			
			return new ImageDetails(
				saveImage(img),
				width,
				height
			);

		} catch (IOException e) {
			throw new IllegalArgumentException("Invalid image data !", e);
		}
	}
	
	public static String toDataURL(byte[] data) {
		if (data == null) return null;
		var encodedData = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(data);
		return encodedData;
	}
	
	public static byte[] computeDarkImage(byte[] data) {
		try {
			var img = ImageIO.read(new ByteArrayInputStream(data));
			var g = img.createGraphics();
			
			var width = img.getWidth();
			var height = img.getHeight();
			
			g.setColor(new Color(0.f, 0.f, 0.f, 0.5f));
			g.fillRect(0, 0, width, height);
			
			return saveImage(img);
			
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid image data !", e);
		}
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
