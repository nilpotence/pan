package fr.nilpo.pan.models.users;

import java.util.Collection;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@PasswordMatch
public class AppUser implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6603294895853673752L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@NotBlank(message = "Vous devez choisir un nom d'utilisateur !")
	@Size(max = 50, message = "Le nom d'utilisateur ne doit pas dépasser 50 caractères !")
	@Pattern(regexp = "^\\S(.*\\S)?$", message = "Le nom d'utilisateur ne doit pas commencer ou finir par un espace !")
	@Column(unique = true, nullable = false)
	private String username;
	
	@NotBlank(message = "Vous devez choisir un mot de passe !")
	// Also checked on the stored BCrypt hash (60 chars) when the entity is persisted.
	@Size(min = 10, max = 72, message = "Le mot de passe doit contenir au moins 10 caractères !")
	@Column(nullable = false)
	private String password;

	@Transient
	private String passwordConfirmation;
	
	public enum Role {
		USER,
		ADMIN
	}
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@OneToOne(optional = false, mappedBy = "user")
	private AppUserScore score;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList("ROLE_" + getRole().name());
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
