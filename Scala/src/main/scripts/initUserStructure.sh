#!/bin/bash

#app=$1 {jo, ...}
#env=$2 {dev, uat, prd}

### à voir plus tard

function create_database
{	
	#space=$3 {raw, app, lake}
	
	beeline -u 'jdbc:hive2://localhost:10000/default' -e "CREATE DATABASE IF NOT EXISTS $2_$3_$1 LOCATION '/$2/$3/$1/hive';"
}

function create_hdfs_structure
{	
	#space=$3 {raw, app, lake}
	#folder_to_create=$4
	hdfs dfs -mkdir -p /$2/$3/$1/$4
	
	###hdfs dfs -chown $1_group $2/$3/$1/$4
	###hdfs dfs -chmod 770 $2/$3/$1/$4
}

echo "Application name= $1"


#création du groupe pour cette nouvelle application
###group add $1_group

#création de l'espace hdfs raw avec affectation des droits au bon groupe
create_hdfs_structure $1 $2 "raw" "hive"
create_hdfs_structure $1 $2 "raw" "input"

#création de l'espace hdfs raw avec affectation des droits au bon groupe
create_hdfs_structure $1 $2 "lake" "hive" 
create_hdfs_structure $1 $2 "lake" "input"

#création de l'espace hdfs raw avec affectation des droits au bon groupe
create_hdfs_structure $1 $2 "app" "hive"
create_hdfs_structure $1 $2 "app" "input"

#création de la base de données raw
create_database $1 $2 "raw"

#création de la base de données lake
create_database $1 $2 "lake"

#création de la base de données app
create_database $1 $2 "app"


echo "Application structure initialized"

