package fr.ffcam.pan.models.boulders;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;

public class ShowBoulderHelper {
	
	public static BoulderBoundingBox getBoulderBoundingBox(Boulder b) {
		try {
			var holds = parseHolds(b.getHolds());
			
			double minX = Double.MAX_VALUE;
			double maxX = - Double.MAX_VALUE;
			double minY = Double.MAX_VALUE;
			double maxY = - Double.MAX_VALUE;
			
			for (var hold : holds) {
				if (Double.compare(hold.x, minX) < 0) minX = hold.x;
				if (Double.compare(hold.x, maxX) > 0) maxX = hold.x;
				
				if (Double.compare(hold.y, minY) < 0) minY = hold.y;
				if (Double.compare(hold.y, maxY) > 0) maxY = hold.y;
			}
			
			minX -= 10;
			maxX += 10;
			minY -= 10;
			maxY += 10;
			
			double width = maxX - minX;
			double height = maxY - minY;
			
			return new BoulderBoundingBox(Math.max(0, minX), Math.max(0, minY), width, height, List.of(holds));
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Hold[] parseHolds(String holds) throws JsonMappingException, JsonProcessingException {
		var mapper = new ObjectMapper();
		return mapper.readValue(holds, Hold[].class);
	}
	
	@Data
	@AllArgsConstructor
	public static class BoulderBoundingBox {
		private double x;
		private double y;
		private double width;
		private double height;
		private List<Hold> holds;
	}
 	
	@Data
	public static class Hold {
		private double x;
		private double y;
	}
	
}
