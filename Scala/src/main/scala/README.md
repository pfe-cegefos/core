
1- Build the project using assembly command on a "terminal" tab:
  > sbt assembly

2- deploy the jar manually on the server

3- Ensure the splited files are present undo /home/cloudera/JO/data folder

4- Execute one of the below "spark-submit" commands:
on server  with cluster mode launched with jar on edge:
spark-submit --master yarn --deploy-mode cluster /home/cloudera/JO/bin/PFE-JO-assembly-1.1.jar /home/cloudera/JO/data/input /dev/raw/JO/data /dev/lake/JO/data

or on server with cluster mode launched with jar on hdfs:
spark-submit --master yarn --deploy-mode cluster scripts/PFE-JO-assembly-1.1.jar /home/cloudera/JO/data/input /dev/raw/JO/data /dev/lake/JO/data

or on cluster with local mode with jar on edge:
spark-submit /home/cloudera/JO/bin/PFE-JO-assembly-1.1.jar /home/cloudera/JO/data/input /dev/raw/JO/data /dev/lake/JO/data
