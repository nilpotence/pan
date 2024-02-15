package fr.ffcam.pan.models.walls;

import javax.persistence.Entity;

import fr.ffcam.pan.models.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Wall extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4202289529344401751L;

	private int width;
	private int height;
	private boolean isArchived;
	
	private String name;
}
