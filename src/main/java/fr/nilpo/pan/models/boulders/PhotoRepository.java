package fr.nilpo.pan.models.boulders;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PhotoRepository extends JpaRepository<Photo, String> {

	@Query(value = "SELECT data FROM pan.photo WHERE id = :id", nativeQuery = true)
	public Optional<byte[]> findDataById(String id);

	/** A photo with the same id has the same bytes: nothing to do if it exists. */
	@Modifying
	@Query(
		value = "INSERT INTO pan.photo (id, data, width, height) VALUES (:id, :data, :width, :height) " +
				"ON CONFLICT (id) DO NOTHING",
		nativeQuery = true
	)
	public int insertIfAbsent(String id, byte[] data, int width, int height);

	@Modifying(flushAutomatically = true)
	@Query(
		value = "DELETE FROM pan.photo p WHERE p.id = :id " +
				"AND NOT EXISTS (SELECT 1 FROM pan.boulder b WHERE b.photo_id = p.id) " +
				"AND NOT EXISTS (SELECT 1 FROM pan.default_photo d WHERE d.photo_id = p.id)",
		nativeQuery = true
	)
	public int deleteIfUnused(String id);
}
