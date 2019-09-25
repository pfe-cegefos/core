/*package fr.cegefos.pfe.controller

import java.text.SimpleDateFormat
import java.util.Date

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
      val type_ = args(0)

      val sparkConf = new SparkConf().setAppName("PFE project - " + type_)
        //.setMaster("local")

      val sparkContext = new SparkContext(sparkConf)
      val sqlContext = new SQLContext(sparkContext)
      //val sparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
      val srcLocal = args(1)
      val srcRaw = args(2)
      val srcLake = args(3)

      //val srcLocal = "Scala\\src\\main\\resources\\local\\output"
      //val srcRaw = "hdfs://192.168.2.16:8020/dev/raw/JO/input"
      //val srcLake = "hdfs://192.168.2.16:8020/data/raw/JO/public"
  /*
      val srcLocal = "D:\\Homeware\\DEV\\pfe-cegefos\\core\\Scala\\src\\main\\resources\\local\\output"
      val srcRaw = "D:\\Homeware\\DEV\\pfe-cegefos\\core\\Scala\\src\\main\\resources\\local\\raw"
      val srcLake = "D:\\Homeware\\DEV\\pfe-cegefos\\core\\Scala\\src\\main\\resources\\local\\lake"
*/
      val hadoopConf = new Configuration()
      hadoopConf.set("fs.defaultFS", "hdfs://localhost:8020")
      //hadoopConf.set("fs.defaultFS", "file://localhost:8020")
      val hdfsFS = FileSystem.get(hadoopConf)

      val localConf = new Configuration()
      localConf.set("fs.defaultFS", "file://localhost")
      val localFS = FileSystem.get(localConf)

      val writer = new Writer(sqlContext, hdfsFS, localFS)
      writer.start(srcLocal, srcRaw, srcLake)
    }
  }
}
*/