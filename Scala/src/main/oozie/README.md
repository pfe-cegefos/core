
#Script to create coordinator or workflow to hdfs and do a dry run
oozie job  -oozie http://quickstart.cloudera:11000/oozie -config job.properties -submit
oozie job -oozie http://quickstart.cloudera:11000/oozie -config job.properties -submit -dryrun <job-id>


#Script to start a PREP job
oozie job -oozie http://quickstart.cloudera:11000/oozie -start <job-id>

#Script to create and start coordinator or workflow to hdfs
oozie job --oozie http://quickstart.cloudera:11000/oozie -config job.properties -run

#Script to rerun workflow
oozie job --oozie http://quickstart.cloudera:11000/oozie -rerun 0000013-190711224707319-oozie-oozi-W -Doozie.wf.rerun.failnodes=false

