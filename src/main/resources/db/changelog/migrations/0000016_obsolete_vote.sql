CREATE TABLE pan.obsolete_vote (
	app_user_id uuid NOT NULL REFERENCES pan.app_user (id) ON DELETE CASCADE,
	boulder_id uuid NOT NULL REFERENCES pan.boulder (id) ON DELETE CASCADE,
	created_at timestamp NOT NULL DEFAULT now(),
	PRIMARY KEY (app_user_id, boulder_id)
);
