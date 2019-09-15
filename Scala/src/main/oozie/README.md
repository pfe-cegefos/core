
#Script to submit coordinator and workflow to hdfs
sudo oozie job --oozie http://quickstart.cloudera:11000/oozie -config job.properties -run

rerun workflow
oozie job --oozie http://quickstart.cloudera:11000/oozie -rerun 0000013-190711224707319-oozie-oozi-W -Doozie.wf.rerun.failnodes=false