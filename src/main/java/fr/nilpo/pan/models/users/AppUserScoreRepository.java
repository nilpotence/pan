package fr.nilpo.pan.models.users;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserScoreRepository extends JpaRepository<AppUserScore, UUID>{

	
}
