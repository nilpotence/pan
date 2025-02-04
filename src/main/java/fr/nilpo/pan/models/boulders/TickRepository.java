package fr.nilpo.pan.models.boulders;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TickRepository extends JpaRepository<Tick, Tick.TickId>{
	
	Page<Tick> findAllByBoulder(Boulder b, Pageable pageable);


    @Query(
        "SELECT " + 
            "new fr.nilpo.pan.models.boulders.BoulderGradeStatsData(" + 
                "t.estimatedGrade, " + 
                "COUNT(*)" +
            ") " +
        "FROM Tick t " + 
        "WHERE t.boulder = :b " + 
        "AND t.estimatedGrade IS NOT NULL " +
        "GROUP BY t.estimatedGrade " + 
        "ORDER BY t.estimatedGrade ASC"
    )
    List<BoulderGradeStatsData> getGradeStatsByBoulder(Boulder b);

	
}
