package fr.nilpo.pan.models.boulders;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;

import fr.nilpo.pan.models.PanEntity;
import fr.nilpo.pan.models.users.AppUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class DefaultPhoto extends PanEntity {
	private static final long serialVersionUID = 8214339180301319373L;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Photo photo;
	
	@Column(nullable = false)
	private LocalDateTime createdAt;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private AppUser createdBy;
}
