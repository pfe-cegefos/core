package fr.cegefos.pfe.controller

import fr.cegefos.pfe.service.FileSpliter
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf

object ApplicationFileSpliter {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    if (args.length == 0) {
      println("dude, i need at least one parameter")
    }
    else {
      val sparkConf = new SparkConf().setAppName("PFE project - FileSpliter").setMaster("local")
      val sparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

      val fileSystem = FileSystem.get(new Configuration())
      val fileSpliter = new FileSpliter(sparkSession, fileSystem)

      fileSpliter.start()
    }
  }
}
