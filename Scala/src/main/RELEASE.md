ACTIONS TO RELEASE THE PROJECT

A-PREPARE THE ENVIRONMENT FROM SCRATCH
cd JO/scripts
./initApplicationStructure.sh jo dev dev_jo

B-DEPLOY THE binaries
0- copy data into the edge-node, oozie files and scripts
structure should be (under home/cloudera path):
  .
  ..
  JO
    data
        input
            games=2016-SUMMER
                xxxx.csv
            games=2014-WINTER
                xxxx.csv
            ...
    bin
        PFE-JO-assembly-1.1.jar
    oozie
         workflow
            workflow.xml
            job.properties
         coordinator
            coordinator.xml
            job.properties
         scripts
            ingestion.sh

1- drop data from frt, app, raw and lake
hdfs dfs -rm -r /dev/ftr/JO/data
hdfs dfs -rm -r /dev/raw/JO/data
hdfs dfs -rm -r /dev/lake/JO/data
hdfs dfs -rm -r /dev/app/JO/data
hdfs dfs -rm /dev/app/JO/bin/*.jar
hdfs dfs -rm /dev/app/JO/oozie/workflow/*.xml
hdfs dfs -rm /dev/app/JO/oozie/coordinator/*.xml
hdfs dfs -rm /dev/app/JO/oozie/workflow/ingestion/scripts/shell/*.sh
hdfs dfs -rm /dev/app/JO/oozie/workflow/ingestion/scripts/hive/*.hql
hdfs dfs -rm /dev/app/JO/oozie/workflow/dataviz/scripts/hive/*.hql


2- push data into tempo zone
hdfs dfs -put /home/cloudera/JO/data/input/* /dev/ftr/JO/data/.

3- push jar binary
hdfs dfs -put -f /home/cloudera/JO/bin/* /dev/app/JO/bin/.

4- push oozies xml and script to execute
hdfs dfs -put -f /home/cloudera/JO/oozie/workflow/ingestion/*.xml /dev/app/JO/oozie/workflow/ingestion/.
hdfs dfs -put -f /home/cloudera/JO/oozie/workflow/dataviz/*.xml /dev/app/JO/oozie/workflow/dataviz/.
hdfs dfs -put -f /home/cloudera/JO/oozie/coordinator/*.xml /dev/app/JO/oozie/coordinator/.
hdfs dfs -put -f /home/cloudera/JO/shell/* /dev/app/JO/oozie/workflow/ingestion/scripts/shell/.
hdfs dfs -put -f /home/cloudera/JO/hive/usc1* /dev/app/JO/oozie/workflow/ingestion/scripts/hive/.
hdfs dfs -put -f /home/cloudera/JO/hive/usc2* /dev/app/JO/oozie/workflow/dataviz/scripts/hive/.

5- create the coordinator + workflow on oozie server
oozie job -oozie http://quickstart.cloudera:11000/oozie -config /home/cloudera/JO/oozie/coordinator/job_ingestion.properties -submit
oozie job -oozie http://quickstart.cloudera:11000/oozie -config /home/cloudera/JO/oozie/workflow/dataviz/job_dataviz.properties -run

--dryrun
oozie job -oozie http://quickstart.cloudera:11000/oozie -config /home/cloudera/JO/oozie/coordinator/job_ingestion.properties -dryrun <job-id>
