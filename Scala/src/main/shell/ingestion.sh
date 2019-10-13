#!/bin/bash

echo "starting at "

spark-submit --conf spark.yarn.maxAppAttempts=1 --master yarn --deploy-mode cluster --executor-memory 512M --num-executors 1 --class fr.cegefos.pfe.controller.ApplicationIngestion /dev/app/JO/bin/PFE-JO-assembly-1.1.jar /dev/ftr/JO/data /dev/raw/JO/data /dev/lake/JO/data

echo "ended at "
