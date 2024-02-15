package fr.ffcam.pan.models.walls;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class WallServiceImpl implements WallService {
	
	@Value("${pan.walls.images.dir:/var/pan/walls/}")
	private String WALL_IMAGES_DIR;
	
	@Autowired
	private WallRepository wallRepository;
	
	@Override
	public Wall storeImage(InputStream stream, String name) throws IOException {
		
			var wall = new Wall();
		
			var bImg = Thumbnails.of(stream)
				.size(1600, 900)
				.asBufferedImage();
			
			var width = bImg.getWidth();
			var height = bImg.getHeight();
			
			wall.setHeight(height);
			wall.setWidth(width);
			wall.setName(name);
			
			doSave(wall.getId(), bImg);
			
			wallRepository.save(wall);
			
			return wall;
	}
	
	private void doSave(UUID id, BufferedImage image) throws IOException {
			var imgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
			var imgWriterParam = imgWriter.getDefaultWriteParam();
			imgWriterParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			imgWriterParam.setCompressionQuality(0.7f);
			
			var file = new File(WALL_IMAGES_DIR + "/" + id.toString());
			file.getParentFile().mkdirs();
			imgWriter.setOutput(new FileImageOutputStream(file));
			imgWriter.write(null, new IIOImage(image, null, null), imgWriterParam);
			imgWriter.dispose();
	}
	
}
