CREATE TABLE pan.tick (
	app_user_id uuid NOT NULL,
	boulder_id uuid NOT NULL,
	created_at timestamp NOT NULL,
	comment TEXT,
	estimated_grade VARCHAR(10),
	PRIMARY KEY (app_user_id, boulder_id)
);
