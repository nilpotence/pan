package fr.nilpo.pan.models.boulders;

import java.util.List;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

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
			
			minX = Math.max(0, minX - 100);
			maxX = Math.min(b.getCustomPhotoWidth(), maxX + 100);
			minY = Math.max(0, minY - 100);
			maxY = Math.min(b.getCustomPhotoHeight(), maxY + 100);
			
			double width = maxX - minX;
			double height = maxY - minY;
			
			return new BoulderBoundingBox(minX, minY, width, height, List.of(holds));
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	// Jackson 3 ignores unknown properties by default; the holds JSON is
	// user input stored as-is, so keep rejecting anything but {x, y}.
	private static final JsonMapper MAPPER = JsonMapper.builder()
			.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
			.build();

	/** @throws tools.jackson.core.JacksonException (unchecked) if the JSON is invalid */
	public static Hold[] parseHolds(String holds) {
		return MAPPER.readValue(holds, Hold[].class);
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
