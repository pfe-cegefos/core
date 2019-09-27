#!/bin/bash

#$1=app {JO, ...}
#$2=env {dev, uat, prd}
#$3=technical_user_id (dev_jo, prd_jo, ...)

userid=$3

function create_database
{
	#space=$3 {raw, app, lake}

	beeline -u 'jdbc:hive2://localhost:10000/default' -e "CREATE DATABASE IF NOT EXISTS $2_$3_$1 LOCATION '/$2/$3/$1/hive';"
}

function create_hdfs_structure
{

	#app=$1 {jo, ...}
    #env=$2 {dev, uat, prd}
	#space=$3 {raw, app, lake}
	#folder_to_create=$4
    #technical user id=$5

	hdfs dfs -mkdir -p /$2/$3/$1/$4

	hdfs dfs -chown $5 /$2/$3/$1/$4
	hdfs dfs -chmod 773 /$2/$3/$1/$4
}

echo "Application name= $1"

###sudo -i
###su -hdfs

#hdfs raw space creation
create_hdfs_structure $1 $2 "raw" "hive" $3
create_hdfs_structure $1 $2 "raw" "data" $3

#hdfs lake space creation
create_hdfs_structure $1 $2 "lake" "hive" $3
create_hdfs_structure $1 $2 "lake" "data" $3

#hdfs app space creation
create_hdfs_structure $1 $2 "app" "hive" $3
create_hdfs_structure $1 $2 "app" "data" $3
create_hdfs_structure $1 $2 "app" "bin" $3
create_hdfs_structure $1 $2 "app" "oozie" $3
create_hdfs_structure $1 $2 "app" "oozie/coordinator" $3
create_hdfs_structure $1 $2 "app" "oozie/workflow" $3
create_hdfs_structure $1 $2 "app" "oozie/scripts" $3
create_hdfs_structure $1 $2 "app" "lib" $3
create_hdfs_structure $1 $2 "app" "logs" $3
create_hdfs_structure $1 $2 "app" "conf" $3


#raw database creation
create_database $1 $2 "raw"

#lake database creation
create_database $1 $2 "lake"

#app database creation
create_database $1 $2 "app"


echo "Application structure initialized"

exit

