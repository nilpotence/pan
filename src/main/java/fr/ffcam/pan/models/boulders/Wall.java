package fr.ffcam.pan.models.boulders;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

//@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Wall {
	
	//@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	private String imagePath;
	
	private double width;
	private double height;
}
