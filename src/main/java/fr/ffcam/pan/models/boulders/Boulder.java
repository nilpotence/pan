package fr.ffcam.pan.models.boulders;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Boulder {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@NotBlank(message = "Vous devez choisir un nom pour votre bloc !")
	private String name;
	
	@NotNull(message = "Vous devez choisir une cotation pour votre bloc !")
	@Pattern(regexp = "^[3-8][a-c]\\+{0,1}$", message = "Vous devez choisir une cotation pour votre bloc !")
	private String estimatedGrade;
	
	@ValidHolds
	private String holds;
}
