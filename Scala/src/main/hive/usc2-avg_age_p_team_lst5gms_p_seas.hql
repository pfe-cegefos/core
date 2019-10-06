DROP TABLE IF EXISTS dev_lake_jo.avg_age_p_team_lst5gms_p_seas;

CREATE TABLE IF NOT EXISTS dev_lake_jo.avg_age_p_team_lst5gms_p_seas
STORED AS PARQUET
AS SELECT
game_type
,year
,team
,AVG(age) average_age
FROM (SELECT
	A.season AS game_type
	,A.year
	,A.team
	,A.ID
	,A.age
	FROM dev_lake_jo.jo_full_games A
	INNER JOIN (SELECT
		games
		,season
		,ROW_NUMBER() over (PARTITION BY season ORDER BY games DESC) rank_year
		FROM dev_lake_jo.jo_full_games
		GROUP BY games, season) B
	ON A.games = B.games AND A.season = B.season
	WHERE B.rank_year <= 5
	GROUP BY A.season, A.year, A.team, A.ID, A.age
	) A
GROUP BY game_type, year, team
;
