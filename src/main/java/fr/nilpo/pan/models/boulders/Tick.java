package fr.nilpo.pan.models.boulders;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.validation.constraints.Pattern;

import fr.nilpo.pan.models.users.AppUser;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Tick {
	
	@EmbeddedId
	private TickId id;

	@ManyToOne(optional = false)
	@MapsId("appUserId")
	private AppUser appUser;
	
	@ManyToOne(optional = false)
	@MapsId("boulderId")
	private Boulder boulder;
	
	private LocalDateTime createdAt;

	@Pattern(regexp = "^[3-8][a-c]\\+{0,1}$", message = "Vous devez choisir une cotation pour votre bloc !")
	private String estimatedGrade;
	
	private String comment;
	
	public Tick() {
		id = new TickId();
	}
	
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode
	@Embeddable
	@Getter
	@Setter
	public static class TickId implements Serializable {
		private static final long serialVersionUID = 2598133279543609413L;
		
		private UUID appUserId;
		private UUID boulderId;
	}
}
