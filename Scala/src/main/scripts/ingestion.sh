#!/usr/bin/env bash

echo "starting at "

spark-submit --master yarn --deploy-mode cluster scripts/PFE-JO-assembly-1.1.jar /home/cloudera/JO/data/input /dev/raw/JO/data /dev/lake/JO/data

echo "ended at "
