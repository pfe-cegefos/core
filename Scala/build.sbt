lazy val root = (project in file(".")).
  settings(
    name := "PFE-JO",
    version := "1.1",
    scalaVersion := "2.11.11",
    mainClass in Compile := Some("fr.cegefos.pfe.controller.ApplicationIngestion")
  )

libraryDependencies += "io.spray" %% "spray-json" % "1.3.4"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.0" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "1.6.0" % "provided"

libraryDependencies += "org.apache.hadoop" % "hadoop-client" % "1.6.0" % "provided"
libraryDependencies += "org.apache.hadoop" % "hadoop-common" % "1.6.0" % "provided"
libraryDependencies += "org.apache.hadoop" % "hadoop-hdfs" % "1.6.0" % "provided"

libraryDependencies += "org.apache.commons" % "commons-io" % "1.3.2" 

libraryDependencies += "com.databricks" % "spark-csv_2.10" % "1.4.0"
libraryDependencies += "org.apache.commons" % "commons-csv" % "1.4"


assemblyMergeStrategy in assembly := {
  case PathList("org", "apache", xs @ _*) => MergeStrategy.last
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
