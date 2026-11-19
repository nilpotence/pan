package fr.nilpo.pan.models;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Transient;

import org.springframework.data.domain.Persistable;

import fr.nilpo.pan.models.users.AppUser;

@MappedSuperclass
public abstract class PanEntity implements Persistable<UUID>, Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 5819669302010467867L;

	@Id
	private UUID id;
	@Transient
	private boolean isNew = true;
	
	public PanEntity() {
		id = UUID.randomUUID();
	}
	
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	@Override
	public boolean isNew() {
		return isNew;
	}
	
	@PrePersist
	@PostLoad
	private void markNotNew() {
		this.isNew = false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		return Objects.equals(id, ((PanEntity)obj).id);
	}

	public abstract AppUser getCreatedBy();
}
