package fr.nilpo.pan.models.boulders;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fr.nilpo.pan.models.users.AppUser;

@Repository
public interface BoulderRepository extends JpaRepository<Boulder, UUID>{

	@EntityGraph(attributePaths = {"score"})
	public Page<Boulder> findAll(Pageable pageable);

    @Query(
        "SELECT new fr.nilpo.pan.models.boulders.BoulderIndexData(" +
            "b.id, b.name, b.estimatedGrade, b.obsoleteCount, b.score.score, t.id IS NOT NULL" +
        ") FROM Boulder b " +
        "LEFT JOIN FETCH Tick t ON t.boulder = b AND t.appUser = :user"
    )
    public Page<BoulderIndexData> findAll(AppUser user, Pageable pageable);
}
