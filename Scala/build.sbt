lazy val root = (project in file(".")).
  settings(
    name := "PFE-JO",
    version := "1.0",
    scalaVersion := "2.11.11",
    mainClass in Compile := Some("fr.cegefos.pfe.controller.Application")
  )

libraryDependencies += "io.spray" %% "spray-json" % "1.3.4"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.2.0" 
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.2.0" 

libraryDependencies += "org.apache.hadoop" % "hadoop-client" % "2.2.0"
libraryDependencies += "org.apache.hadoop" % "hadoop-common" % "2.2.0" 
libraryDependencies += "org.apache.hadoop" % "hadoop-hdfs" % "2.2.0"

libraryDependencies += "org.apache.commons" % "commons-io" % "1.3.2"

libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.2.0"
libraryDependencies += "org.mongodb.spark" %% "mongo-spark-connector" % "2.2.0"

libraryDependencies ++= Seq( "joda-time" % "joda-time"    % "2.3"
  , "org.joda"  % "joda-convert" % "1.6"
)