package fr.cegefos.pfe.controller

import fr.cegefos.pfe.service.{DatalakeWriter, FileSpliter, MongoDBWriter}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object Application {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    if (args.length == 0) {
      println("dude, i need at least one parameter")
    }
    else {
      val type_ = args(0)

      val sparkConf = new SparkConf().setAppName("PFE project - " + type_)
      val sparkSession = SparkSession.builder()
        .config(sparkConf)
        .master("local[2]")
        .getOrCreate()

      val fileSystem = FileSystem.get(new Configuration())

      type_ match {
        case "split" => {

          val fileSpliter = new FileSpliter(sparkSession, fileSystem)
          fileSpliter.start()
        }

        case "ingestion" => {
          val srcLocal = args(1)
          val srcRaw = args(2)
          val srcLake = args(3)

          //val srcLocal = "Scala\\src\\main\\resources\\local\\output"
          //val srcRaw = "hdfs://192.168.2.16:8020/dev/raw/JO/input"
          //val srcLake = "hdfs://192.168.2.16:8020/data/raw/JO/public"

          val hadoopConf = new Configuration()
          hadoopConf.set("fs.defaultFS", "hdfs://192.168.0.28:8020")
          val hdfsFS = FileSystem.get(hadoopConf)

          val localConf = new Configuration()
          localConf.set("fs.defaultFS", "file://192.168.0.28")
          val localFS = FileSystem.get(localConf)

          val datalakeWriter = new DatalakeWriter(sparkSession, hdfsFS, localFS)
          datalakeWriter.start(srcLocal, srcRaw, srcLake)
        }

        case "mongodb" => {
          val sparkSession = SparkSession.builder()
            .config(sparkConf)
            .master("local[2]")
            .config("spark.mongodb.input.uri", "mongodb://localhost:27017/JO.LastGames")
            .config("spark.mongodb.input.readPreference.name", "secondaryPreferred")
            .config("spark.mongodb.output.uri", "mongodb://localhost:27017/JO.LastGames")
            .getOrCreate()

          val mongoDBWriter = new MongoDBWriter(sparkSession)
          mongoDBWriter.start

        }
      }
    }

  }
}
