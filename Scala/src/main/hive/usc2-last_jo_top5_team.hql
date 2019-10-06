DROP TABLE IF EXISTS dev_lake_jo.last_jo_top5_team;

CREATE TABLE IF NOT EXISTS dev_lake_jo.last_jo_top5_team
STORED AS PARQUET
AS SELECT
   rank_global AS rank
   ,team
   ,gold
   ,silver
   ,bronze
   ,gold + silver + bronze AS medals
   FROM (SELECT
           C.*
           ,ROW_NUMBER() over (ORDER BY rank_medal DESC) rank_global
   		,CAST(rank_medal / 1000000 AS INT) AS gold
   		,CAST((rank_medal % 1000000) / 1000 AS INT) AS silver
   		,CAST(rank_medal % 1000 AS INT) AS bronze
           FROM (SELECT
                   team
                   ,SUM(medalscore) rank_medal
                   FROM (SELECT
   						team
                           ,(CASE WHEN medal = "Gold" THEN 1000000
                                   WHEN medal = "Silver" THEN 1000
                                   WHEN medal = "Bronze" THEN 1
                                   ELSE 0 END) AS medalscore
                           FROM (SELECT team, event, medal, games
   							FROM dev_lake_jo.jo_full_games A
   							INNER JOIN (SELECT MAX(games) max_games FROM dev_lake_jo.jo_full_games) B
   							ON A.games = B.max_games
   							GROUP BY team, event, medal, games) A
                           ) B
                   GROUP BY team
                   ) C
           ) D
   WHERE rank_global <= 5
   ;