
1- Build the project using assembly command on a "terminal" tab:
  > sbt assembly

2- deploy the jar manually on the server

3- Ensure the splited files are present undo /home/cloudera/JO/data folder

4- Execute one of the below "spark-submit" commands:
on server  with cluster mode launched with jar on edge:
spark-submit --master yarn --deploy-mode cluster /home/cloudera/JO/bin/PFE-JO-assembly-1.1.jar /home/cloudera/JO/data/input /dev/raw/JO/data /dev/lake/JO/data

or on server with cluster mode launched with jar on hdfs:
spark-submit --conf spark.yarn.maxAppAttempts=1 --master yarn --deploy-mode cluster /home/cloudera/JO/bin/PFE-JO-assembly-1.1.jar /dev/ftr/JO/data/ /dev/raw/JO/data /dev/lake/JO/data

or on cluster with local mode with jar on edge:
spark-submit /home/cloudera/JO/bin/PFE-JO-assembly-1.1.jar /home/cloudera/JO/data/data /dev/raw/JO/data /dev/lake/JO/data




<!--


	<action name="spark-ingestion">
    		<spark xmlns="uri:oozie:spark-action:0.2">
    			<job-tracker>${jobTracker}</job-tracker>
    			<name-node>${nameNode}</name-node>
    			<configuration>
    				<property>
    					<name>mapred.compress.map.output</name>
    					<value>true</value>
    				</property>
    			</configuration>
    			<master>yarn-cluster</master>
    			<mode>cluster</mode>
    			<name>Spark-JO</name>
    			<class>fr.cegefos.pfe.controller.ApplicationIngestion</class>
    			<jar>/dev/app/JO/bin/PFE-JO-assembly-1.1.jar</jar>
    			<arg>/dev/ftr/JO/data</arg>
    			<arg>/dev/raw/JO/data</arg>
    			<arg>/dev/lake/JO/data</arg>
    		</spark>
    		<ok to="end"/>
    		<error to="fail"/>
    	</action>
-->