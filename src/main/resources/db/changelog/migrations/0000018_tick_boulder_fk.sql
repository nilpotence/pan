-- Ticks had no foreign key to their boulder: deleting a boulder left its
-- ticks behind. Drop those orphans, then let the database delete a boulder's
-- ticks with it (the boulder is deleted with a bulk query, see
-- BoulderRepository.deleteBoulder, so JPA cascades wouldn't run anyway).
DELETE FROM pan.tick t
WHERE NOT EXISTS (SELECT 1 FROM pan.boulder b WHERE b.id = t.boulder_id);

ALTER TABLE pan.tick
	ADD CONSTRAINT tick_boulder_id_fkey
	FOREIGN KEY (boulder_id) REFERENCES pan.boulder (id) ON DELETE CASCADE;

-- Ticks are looked up by boulder (boulder page, scores); the primary key
-- (app_user_id, boulder_id) can't serve those lookups.
CREATE INDEX tick_boulder_id_idx ON pan.tick (boulder_id);
