DROP TABLE IF EXISTS dev_lake_jo.jo_full_games;

CREATE TABLE IF NOT EXISTS dev_lake_jo.jo_full_games
STORED AS PARQUET
LOCATION '/dev/lake/JO/hive'
AS 
SELECT * 
FROM dev_raw_jo.jo_full_games;
