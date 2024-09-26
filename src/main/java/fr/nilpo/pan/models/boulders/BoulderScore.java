package fr.nilpo.pan.models.boulders;


import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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
