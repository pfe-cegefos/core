DROP TABLE IF EXISTS dev_lake_jo.jo_full_games;

CREATE EXTERNAL TABLE IF NOT EXISTS dev_lake_jo.jo_full_games(
    ID  String,
    Name  String,
    Sex  String,
    Age  String,
    Height  String,
    Weight  String,
    Team  String,
    NOC  String,
    Year  String,
    Season  String,
    City  String,
    Sport  String,
    Event  String,
    Medal  String
)
PARTITIONED BY (`games` String)
ROW FORMAT DELIMITED
STORED AS PARQUET
LOCATION '/dev/lake/JO/data';

MSCK REPAIR TABLE dev_raw_jo.jo_full_games;
