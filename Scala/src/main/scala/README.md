
1- Build the project using assembly command on a "terminal" tab:
  > sbt assembly

2- deploy the jar manually on the server

3- Ensure the splited files are present undo /home/cloudera/JO/data folder

4- Execute one of the below "spark-submit" commands :
spark-submit --conf spark.sql.parquet.writeLegacyFormat=true --master yarn --deploy-mode cluster ~/JO/bin/PFE-JO-assembly-1.1.jar ingestion /home/cloudera/JO/data/input /dev/raw/JO/data /dev/lake/JO/data
or
spark-submit --conf spark.sql.parquet.writeLegacyFormat=true --master yarn --deploy-mode cluster scripts/PFE-JO-assembly-1.1.jar ingestion /home/cloudera/JO/data/input /dev/raw/JO/data /dev/lake/JO/data