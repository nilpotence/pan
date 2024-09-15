package fr.nilpo.pan.models.boulders;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DefaultPhotoRepository extends JpaRepository<DefaultPhoto, UUID> {

	public Optional<DefaultPhoto> findFirstByOrderByCreatedAtDesc();
}
