DROP TABLE IF EXISTS dev_lake_jo.last_jo_avg_age;

CREATE TABLE IF NOT EXISTS dev_lake_jo.last_jo_avg_age
STORED AS PARQUET
AS SELECT
C.year
,AVG(C.age) average_age
FROM (SELECT
	A.year
	,A.ID
	,A.age
	FROM dev_lake_jo.jo_full_games A
	INNER JOIN (SELECT MAX(games) max_game FROM dev_lake_jo.jo_full_games) B
	ON A.games = B.max_game
	GROUP BY A.year,A.ID,A.age) C
GROUP BY C.year
;