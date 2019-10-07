#!/bin/sh

#$1=app {JO, ...}
#$2=env {dev, uat, prd}
#$3=technical_user_id (dev_jo, prd_jo, ...)

app=$1
env=$2
userid=$3

function create_database
{
	#space=$3 {raw, app, lake}
    app=$1
    env=$2
    space=$3

    database_name=$env\_$space\_${app,,}
    folder=/$env/$space/$app/hive

	beeline -u 'jdbc:hive2://localhost:10000/default' -e "DROP DATABASE IF EXISTS $database_name CASCADE;"
	beeline -u 'jdbc:hive2://localhost:10000/default' -e "CREATE DATABASE IF NOT EXISTS $database_name LOCATION '$folder';"

	echo "Database $database_name created."
}

function create_hdfs_structure
{
	#app=$1 {jo, ...}
    #env=$2 {dev, uat, prd}
	#space=$3 {raw, app, lake}
	#folder to create=$4
    #technical user id=$5

    app=$1
    env=$2
    space=$3
    folder=$4
    user=$5

    path=/$env/$space/$app/$folder

    ###hdfs dfs -rm -r $path
	hdfs dfs -mkdir -p $path

	###hdfs dfs -chown $user $path
	###hdfs dfs -chmod 773 $path

	echo "Folder $path created."
}

echo "Application structure initialization ..."

echo "Application name = $1"

#hdfs ftr space creation
create_hdfs_structure $app $env "ftr" "data" $userid

#hdfs raw space creation
create_hdfs_structure $app $env "raw" "hive" $userid
create_hdfs_structure $app $env "raw" "data" $userid

#hdfs lake space creation
create_hdfs_structure $app $env "lake" "hive" $userid
create_hdfs_structure $app $env "lake" "data" $userid

#hdfs app space creation
create_hdfs_structure $app $env "app" "hive" $userid
create_hdfs_structure $app $env "app" "data" $userid
create_hdfs_structure $app $env "app" "bin" $userid
create_hdfs_structure $app $env "app" "lib" $userid
create_hdfs_structure $app $env "app" "logs" $userid
create_hdfs_structure $app $env "app" "conf" $userid
create_hdfs_structure $app $env "app" "oozie" $userid
create_hdfs_structure $app $env "app" "oozie/workflow" $userid
create_hdfs_structure $app $env "app" "oozie/workflow/scripts" $userid
create_hdfs_structure $app $env "app" "oozie/coordinator" $userid

#raw database creation
create_database $app $env "raw"

#lake database creation
create_database $app $env "lake"

#app database creation
create_database $app $env "app"

echo "Application structure initialized"

exit
