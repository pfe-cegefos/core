DROP TABLE IF EXISTS dev_lake_jo.last_jo_top5_f;

CREATE TABLE IF NOT EXISTS dev_lake_jo.last_jo_top5_f
STORED AS PARQUET
AS SELECT
rank_global AS rank
,name
,gold
,silver
,bronze
,gold + silver + bronze AS medals
FROM (SELECT
        B.*
        ,ROW_NUMBER() over (ORDER BY rank_medal DESC) rank_global
		,CAST(rank_medal / 1000000 AS INT) AS gold
		,CAST((rank_medal % 1000000) / 1000 AS INT) AS silver
		,CAST(rank_medal % 1000 AS INT) AS bronze
        FROM (SELECT
                ID
                ,name
                ,SUM(medalscore) rank_medal
                FROM (SELECT
						ID
                        ,name
                        ,(CASE WHEN medal = "Gold" THEN 1000000
                                WHEN medal = "Silver" THEN 1000
                                WHEN medal = "Bronze" THEN 1
                                ELSE 0 END) AS medalscore
                        FROM dev_lake_jo.jo_full_games A
						INNER JOIN (SELECT MAX(year) max_year FROM dev_lake_jo.jo_full_games) B
						ON A.year = B.max_year
						WHERE sex = "F"
                        ) A
                GROUP BY ID,name
                ) B
        ) C
WHERE rank_global <= 5
;
