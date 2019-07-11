DROP TABLE IF EXISTS dev_raw_jo.jo_full_games;

CREATE EXTERNAL TABLE IF NOT EXISTS dev_raw_jo.jo_full_games(
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
PARTITIONED BY (Games String)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\;'
STORED AS TEXTFILE
LOCATION '/dev/raw/JO/input'
TBLPROPERTIES ("skip.header.line.count"="1");

MSCK REPAIR TABLE dev_raw_jo.jo_full_games;
SHOW PARTITIONS dev_raw_jo.jo_full_games;
