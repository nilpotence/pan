package fr.nilpo.pan.models.users;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * The logged-in user as stored in the session (security context): only what
 * Spring Security needs, never the AppUser entity. The entity is reloaded
 * from the database when needed (AuthService.getCurrentUser), so it is always
 * up to date (score, rank) and the session stays small.
 *
 * The password hash is only there while logging in (checked by Spring
 * Security, then erased before the session is saved).
 */
public class PanUserDetails implements UserDetails, CredentialsContainer {

	private static final long serialVersionUID = 6519813187563394812L;

	private final UUID id;
	private final String username;
	private final AppUser.Role role;
	private String password;

	private PanUserDetails(AppUser user, String password) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.role = user.getRole();
		this.password = password;
	}

	/** For the login form and remember-me, which check the password hash. */
	public static PanUserDetails withPassword(AppUser user) {
		return new PanUserDetails(user, user.getPassword());
	}

	/** For a user logged in programmatically (after signup). */
	public static PanUserDetails withoutPassword(AppUser user) {
		return new PanUserDetails(user, null);
	}

	public UUID getId() {
		return id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList("ROLE_" + role.name());
	}

	@Override
	public void eraseCredentials() {
		password = null;
	}
}
