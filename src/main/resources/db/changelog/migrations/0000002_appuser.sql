CREATE TABLE pan.app_user (
	id uuid primary key,
	username varchar(255) not null,
	password text not null
)