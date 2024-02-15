package fr.ffcam.pan.models.walls;

import java.io.IOException;
import java.io.InputStream;

public interface WallService {

	public Wall storeImage(InputStream stream, String name) throws IOException;
}
