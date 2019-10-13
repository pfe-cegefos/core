#!/bin/bash

#$1=local src path (ex: /home/cloudera/JO/data)
#$2=hdfs dest path (ex: /dev/raw/jo/input)


function copy
{
	for i in `ls $1`
	do
		echo "source = $1 and destination = $2"

		if [ -d "$i" ]
		then
			l $1/$i $2;
		else

			subfolder=$(echo "$i" | cut -f 1 -d '.')
			echo "subfolder $subfolder"
			folder=$2"/games="$subfolder
			echo "folder $folder"

			echo "copying file $1/$i into $folder/$i ..."

			hdfs dfs -mkdir $folder
			hdfs dfs -copyFromLocal $1/$i $folder/$i
		fi
	done
}

copy $1 $2

echo "All copy finished"
