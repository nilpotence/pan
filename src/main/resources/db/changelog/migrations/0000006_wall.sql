CREATE TABLE IF NOT EXISTS pan.wall (
	id uuid primary key,
	image_path varchar(255) not null,
	width integer not null,
	height integer not null,
	is_archived boolean not null
)