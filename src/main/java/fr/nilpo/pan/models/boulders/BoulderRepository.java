package fr.nilpo.pan.models.boulders;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoulderRepository extends JpaRepository<Boulder, UUID>{

	@EntityGraph(attributePaths = {"score"})
	public Page<Boulder> findAll(Pageable pageable);
	
}
