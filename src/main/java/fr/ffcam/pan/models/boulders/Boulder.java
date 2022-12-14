package fr.ffcam.pan.models.boulders;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import fr.ffcam.pan.models.PanEntity;
import fr.ffcam.pan.models.users.AppUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Boulder implements PanEntity {
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
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private AppUser createdBy;
	
	@Column(nullable = false)
	private LocalDateTime createdAt;

	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	private AppUser lastUpdatedBy;
	
	private LocalDateTime lastUpdatedAt;
}
