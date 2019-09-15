spark-submit --conf spark.sql.parquet.writeLegacyFormat=true --master yarn --deploy-mode cluster  pfe-jo_2.11-1.0.jar ingestion /home/cloudera/JO/data /dev/raw/JO/data /dev/lake/JO/data
