-- Photos are stored once and shared: boulders and default photos reference
-- a row of pan.photo instead of each holding a copy of the JPEG.
-- The id is the hex SHA-256 of the JPEG bytes, so identical photos collapse
-- into one row, and /photos/{id} is a URL whose content never changes.
CREATE TABLE pan.photo (
	id varchar(64) PRIMARY KEY,
	data bytea NOT NULL,
	width integer NOT NULL,
	height integer NOT NULL,
	created_at timestamp NOT NULL DEFAULT now()
);

INSERT INTO pan.photo (id, data, width, height)
SELECT DISTINCT ON (1) encode(sha256(data), 'hex'), data, width, height
FROM pan.default_photo
ON CONFLICT (id) DO NOTHING;

INSERT INTO pan.photo (id, data, width, height)
SELECT DISTINCT ON (1) encode(sha256(custom_photo), 'hex'), custom_photo, custom_photo_width, custom_photo_height
FROM pan.boulder
ON CONFLICT (id) DO NOTHING;

ALTER TABLE pan.default_photo ADD COLUMN photo_id varchar(64) REFERENCES pan.photo (id);
UPDATE pan.default_photo SET photo_id = encode(sha256(data), 'hex');
ALTER TABLE pan.default_photo
	ALTER COLUMN photo_id SET NOT NULL,
	DROP COLUMN data,
	DROP COLUMN width,
	DROP COLUMN height;

ALTER TABLE pan.boulder ADD COLUMN photo_id varchar(64) REFERENCES pan.photo (id);
UPDATE pan.boulder SET photo_id = encode(sha256(custom_photo), 'hex');
ALTER TABLE pan.boulder
	ALTER COLUMN photo_id SET NOT NULL,
	DROP COLUMN custom_photo,
	DROP COLUMN custom_photo_width,
	DROP COLUMN custom_photo_height;

CREATE INDEX boulder_photo_id_idx ON pan.boulder (photo_id);
CREATE INDEX default_photo_photo_id_idx ON pan.default_photo (photo_id);
