package fr.nilpo.pan.models.boulders;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    /** @return 1 if the vote was recorded, 0 if this user had already voted. */
    @Modifying
    @Query(
        value = "INSERT INTO pan.obsolete_vote (app_user_id, boulder_id) VALUES (:userId, :boulderId) " +
                "ON CONFLICT DO NOTHING",
        nativeQuery = true
    )
    public int addObsoleteVote(UUID userId, UUID boulderId);

    @Modifying
    @Query(value = "DELETE FROM pan.obsolete_vote WHERE boulder_id = :boulderId", nativeQuery = true)
    public int clearObsoleteVotes(UUID boulderId);

    @Modifying
    @Query("UPDATE Boulder b SET b.obsoleteCount = b.obsoleteCount + 1 WHERE b.id = :boulderId")
    public int incrementObsoleteCount(UUID boulderId);

    @Modifying
    @Query("UPDATE Boulder b SET b.obsoleteCount = 0 WHERE b.id = :boulderId")
    public int resetObsoleteCount(UUID boulderId);
}
