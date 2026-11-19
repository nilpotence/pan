package fr.nilpo.pan.models.boulders;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A JPEG shared by boulders and default photos. The id is the hex SHA-256 of
 * the bytes: identical photos are one row, and the content behind an id never
 * changes (served from /photos/{id} with an immutable cache).
 *
 * Only the metadata is mapped, so loading a boulder never loads the image.
 * The bytes are read and written through {@link PhotoRepository}'s native
 * queries, and rows are only created by {@link PhotoService#save}.
 */
@Entity
@Immutable
@Getter
@NoArgsConstructor
public class Photo implements Serializable {
	private static final long serialVersionUID = 4021399853155874317L;

	@Id
	private String id;

	private int width;

	private int height;
}
