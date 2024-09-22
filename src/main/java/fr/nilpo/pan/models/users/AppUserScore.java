package fr.nilpo.pan.models.users;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.springframework.data.annotation.Immutable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity(name ="app_user_score")
@Immutable
@Getter
@RequiredArgsConstructor
public class AppUserScore {
	
	@Id
	private UUID appUserId;
	
	@JoinColumn(referencedColumnName = "id", insertable = false, name = "app_user_id")
	@OneToOne
	private AppUser user;
	
	private long score;
}
