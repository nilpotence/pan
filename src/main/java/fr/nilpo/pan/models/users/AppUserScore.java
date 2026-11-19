package fr.nilpo.pan.models.users;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import org.springframework.data.annotation.Immutable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity(name ="app_user_score")
@Immutable
@Getter
@RequiredArgsConstructor
public class AppUserScore implements Serializable {
	// Reachable from the logged-in AppUser, which is stored in the HTTP
	// session (security context): it must be serializable for the session
	// to be saved, e.g. across devtools restarts.
	private static final long serialVersionUID = -2364711086425573092L;
	
	@Id
	private UUID appUserId;
	
	@JoinColumn(referencedColumnName = "id", insertable = false, name = "app_user_id")
	@OneToOne(optional = false)
	private AppUser user;
	
	private long score;
	
	private long rank;
}
