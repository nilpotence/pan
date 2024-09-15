CREATE TABLE pan.default_photo (
	id uuid primary key,
	data bytea not null,
	width integer not null,
	height integer not null,
	created_at timestamp not null,
	created_by_id uuid REFERENCES pan.app_user(id) NOT NULL
);