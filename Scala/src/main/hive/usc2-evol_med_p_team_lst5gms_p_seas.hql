DROP TABLE IF EXISTS dev_lake_jo.evol_med_p_team_lst5gms_p_seas;

CREATE TABLE IF NOT EXISTS dev_lake_jo.evol_med_p_team_lst5gms_p_seas
STORED AS PARQUET
AS SELECT
game_type
,year
,team
,gold
,silver
,bronze
FROM (SELECT
        C.*
		,CAST(rank_medal / 1000000 AS INT) AS gold
		,CAST((rank_medal % 1000000) / 1000 AS INT) AS silver
		,CAST(rank_medal % 1000 AS INT) AS bronze
        FROM (SELECT
                game_type
				,year
				,team
                ,SUM(medalscore) rank_medal
                FROM (SELECT
						A.season AS game_type
						,A.year
						,A.team
                        ,(CASE WHEN medal = "Gold" THEN 1000000
                                WHEN medal = "Silver" THEN 1000
                                WHEN medal = "Bronze" THEN 1
                                ELSE 0 END) AS medalscore
                        FROM (SELECT team, event, medal, A.games, A.season, year
							FROM dev_lake_jo.jo_full_games A
							INNER JOIN (SELECT
								games
								,season
								,ROW_NUMBER() over (PARTITION BY season ORDER BY games DESC) rank_year
								FROM dev_lake_jo.jo_full_games
								GROUP BY games, season) B
							ON A.games = B.games AND A.season = B.season
							WHERE B.rank_year <= 5
							GROUP BY team, event, medal, A.games, A.season, year) A
                        ) B
                GROUP BY game_type, year, team
                ) C
        ) D
;
