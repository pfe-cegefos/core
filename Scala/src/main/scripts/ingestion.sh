#!/usr/bin/env bash

echo "starting at "

spark-submit --conf spark.sql.parquet.writeLegacyFormat=true --master yarn --deploy-mode cluster scripts/PFE-JO-assembly-1.1.jar ingestion /home/cloudera/JO/data/input /dev/raw/JO/data /dev/lake/JO/data

echo "ended at "
