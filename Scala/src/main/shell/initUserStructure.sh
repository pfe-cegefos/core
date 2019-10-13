#!/bin/bash

#$1=userid
##We suppose that user also created on ldap/kerberos system with all necessary information


echo "User name= $1"
sudo -i
su - hdfs

hdfs dfs -mkdir /user/$1
hdfs dfs -chown $1 /user/$1
hdfs dfs -chmod 750 /user/$1

echo "User structure created"

exit
