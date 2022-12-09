ALTER TABLE pan.boulder
ADD COLUMN created_by_id uuid REFERENCES pan.app_user(id) NOT NULL,
ADD COLUMN created_at timestamp without time zone NOT NULL,
ADD COLUMN last_updated_by_id uuid REFERENCES pan.app_user(id),
ADD COLUMN last_updated_at timestamp without time zone;