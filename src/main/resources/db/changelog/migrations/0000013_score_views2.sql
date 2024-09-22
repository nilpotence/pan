CREATE OR REPLACE VIEW pan.boulder_score AS
SELECT *, rank() OVER (ORDER BY score desc)
FROM (
	SELECT 
		b.id AS boulder_id,
		count(t.app_user_id) AS nb_ticks,
    	(1000::real / COALESCE(NULLIF(count(t.app_user_id), 0), 1::real))::bigint AS score
	FROM pan.boulder b
	LEFT JOIN pan.tick t ON b.id = t.boulder_id
  	GROUP BY b.id
  	);

CREATE OR REPLACE VIEW pan.app_user_score AS
SELECT *, rank() OVER (ORDER BY score desc)
FROM (
	SELECT 
		u.id AS app_user_id,
		COALESCE(sum(bscore.score), 0::bigint)::bigint AS score
	FROM pan.app_user u
	LEFT JOIN pan.tick t ON t.app_user_id = u.id
	LEFT JOIN pan.boulder_score bscore ON bscore.boulder_id = t.boulder_id
	GROUP BY u.id
	);

	

