package fr.ffcam.pan.models.boulders;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoulderRepository extends JpaRepository<Boulder, UUID>{

}
