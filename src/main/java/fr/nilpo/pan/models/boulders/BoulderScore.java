package fr.nilpo.pan.models.boulders;


import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import org.springframework.data.annotation.Immutable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity(name = "boulder_score")
@Immutable
@Getter
@RequiredArgsConstructor
public class BoulderScore {
	
	@Id
	private UUID boulderId;
	
	@JoinColumn(referencedColumnName = "id", insertable = false, name = "boulder_id")
	@OneToOne(optional = false)
	private Boulder boulder;

	private long nb_ticks;
	
	private long score;
	
	private long rank;
	
}
