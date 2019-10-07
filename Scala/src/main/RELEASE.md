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
        done
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
hdfs dfs -rm /dev/app/JO/bin/*
hdfs dfs -rm /dev/app/JO/oozie/workflow/*
hdfs dfs -rm /dev/app/JO/oozie/coordinator/*
hdfs dfs -rm /dev/app/JO/oozie/scripts/*

push data into tempo zone
hdfs dfs -put /home/cloudera/JO/data/input/* /dev/ftr/JO/data/.

2- push jar binary
hdfs dfs -put /home/cloudera/JO/bin/* /dev/app/JO/bin/.

3- push oozies xml and script to execute
hdfs dfs -put /home/cloudera/JO/oozie/workflow/workflow.xml /dev/app/JO/oozie/workflow/.
hdfs dfs -put /home/cloudera/JO/oozie/coordinator/coordinator.xml /dev/app/JO/oozie/coordinator/.
hdfs dfs -put /home/cloudera/JO/oozie/scripts/* /dev/app/JO/oozie/workflow/scripts/.

4- create the coordinator + workflow on oozie server
oozie job  -oozie http://quickstart.cloudera:11000/oozie -config /home/cloudera/JO/oozie/coordinator/job.properties -submit
oozie job -oozie http://localhost:11000/oozie -config /home/cloudera/JO/oozie/coordinator/job.properties -dryrun <job-id>
