name := "PFE"

version := "0.1"

scalaVersion := "2.11.11"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.2.0"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.2.0"
libraryDependencies += "org.apache.hadoop" % "hadoop-client" % "2.2.0"
libraryDependencies += "io.spray" %% "spray-json" % "1.3.4"

libraryDependencies += "org.apache.hadoop" % "hadoop-common" % "2.2.0" 
libraryDependencies += "org.apache.hadoop" % "hadoop-hdfs" % "2.2.0"
libraryDependencies += "org.apache.commons" % "commons-io" % "1.3.2" 