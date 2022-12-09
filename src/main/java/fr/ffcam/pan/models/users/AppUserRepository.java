package fr.ffcam.pan.models.users;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID>{
	public Optional<AppUser> findByUsername(String username);
}
