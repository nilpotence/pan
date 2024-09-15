ALTER TABLE pan.boulder
DROP COLUMN custom_photo,
ADD COLUMN custom_photo bytea,
ADD COLUMN custom_photo_width INTEGER,
ADD COLUMN custom_photo_height INTEGER;
