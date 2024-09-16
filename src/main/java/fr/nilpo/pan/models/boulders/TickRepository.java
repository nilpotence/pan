package fr.nilpo.pan.models.boulders;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TickRepository extends JpaRepository<Tick, Tick.TickId>{
	
	Page<Tick> findAllByBoulder(Boulder b, Pageable pageable);
	
}
