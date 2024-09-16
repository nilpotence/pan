package fr.nilpo.pan.models;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

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
