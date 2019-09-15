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

	hdfs dfs -chown $5 $2/$3/$1/$4
	hdfs dfs -chmod 773 $2/$3/$1/$4
}

echo "Application name= $1"

sudo -i
su - hdfs

#création de l'espace hdfs raw avec affectation des droits au bon groupe
create_hdfs_structure $1 $2 "raw" "hive" $3
create_hdfs_structure $1 $2 "raw" "data" $3

#création de l'espace hdfs raw avec affectation des droits au bon groupe
create_hdfs_structure $1 $2 "lake" "hive" $3
create_hdfs_structure $1 $2 "lake" "data" $3

#création de l'espace hdfs raw avec affectation des droits au bon groupe
create_hdfs_structure $1 $2 "app" "hive" $3
create_hdfs_structure $1 $2 "app" "data" $3
create_hdfs_structure $1 $2 "app" "bin" $3

#création de la base de données raw
create_database $1 $2 "raw"

#création de la base de données lake
create_database $1 $2 "lake"

#création de la base de données app
create_database $1 $2 "app"


echo "Application structure initialized"

exit

