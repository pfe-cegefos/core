package fr.cegefos.pfe.controller

import fr.cegefos.pfe.service.Ingestion
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{SQLContext}

object ApplicationIngestion {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    if (args.length == 0) {
      println("dude, i need at least one parameter")
    }
    else {
      val sparkConf = new SparkConf().setAppName("PFE Project - Ingestion")
        .set("spark.sql.parquet.writeLegacyFormat", "true")
        //.setMaster("local") //TODO REMOVE FOR CLUSTER

      val sparkContext = new SparkContext(sparkConf)
      val sqlContext = new SQLContext(sparkContext)
      //sqlContext.setConf("spark.sql.parquet.writeLegacyFormat", "true")


      val srcLocal = args(0)
      val srcRaw = args(1)
      val srcLake = args(2)

/*      val srcLocal = "D:\\Homeware\\DEV\\pfe-cegefos\\core\\Scala\\src\\main\\resources\\local\\output"  //TODO REMOVE FOR CLUSTER
      val srcRaw = "D:\\Homeware\\DEV\\pfe-cegefos\\core\\Scala\\src\\main\\resources\\local\\raw"
      val srcLake = "D:\\Homeware\\DEV\\pfe-cegefos\\core\\Scala\\src\\main\\resources\\local\\lake"
*/
      val hadoopConf = new Configuration()
      hadoopConf.set("fs.defaultFS", "hdfs://localhost:8020")
      //hadoopConf.set("fs.defaultFS", "file://localhost:8020") //TODO REMOVE FOR CLUSTER
      val hdfsFS = FileSystem.get(hadoopConf)

      val localConf = new Configuration()
      localConf.set("fs.defaultFS", "hdfs://localhost:8020")
      //localConf.set("fs.defaultFS", "file://localhost") //TODO REMOVE FOR CLUSTER

      val localFS = FileSystem.get(localConf)

      val ingestion = new Ingestion(sqlContext, hdfsFS, localFS)
      ingestion.start(srcLocal, srcRaw, srcLake)
    }
  }
}
