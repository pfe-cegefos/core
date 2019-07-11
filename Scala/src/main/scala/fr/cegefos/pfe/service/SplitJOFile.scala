package fr.cegefos.pfe.service

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.hadoop.fs._;


import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path

class SplitJOFile {

  val srcPath = "D:\\Homeware\\DEV\\pfe-cegefos\\core\\Scala\\src\\main\\resources\\local\\JO"
  val destPath = "D:\\Homeware\\DEV\\pfe-cegefos\\core\\Scala\\src\\main\\resources\\local\\JO\\tmp"
  val finalPath = "D:\\Homeware\\DEV\\pfe-cegefos\\core\\Scala\\src\\main\\resources\\local\\JO\\done"

  def start() {
    Logger.getLogger("org").setLevel(Level.ERROR)

    val sparkSession = SparkSession.builder().appName("PFE project Split").master("local[2]").getOrCreate()

    val df = sparkSession.read.format("CSV").option("header", true).option("sep", ";").load(srcPath)

    df
      .coalesce(1)
      .write
      .partitionBy("Games")
      .option("header", "true")
      .option("delimiter", ";")
      .csv(destPath)

    renaming
  }

  def renaming: Unit ={
    val fs = FileSystem.get(new Configuration())
    val files = fs.listFiles(new Path(destPath), true)

    while (files.hasNext()) {
      val file = files.next()

      if(file.getPath.getName.split("\\.").last == "csv") {
        val fileName = file.getPath.getParent.getName.replace("Games=","").replace("%20","-")
        fs.rename(file.getPath, new Path(finalPath + "//jo-" + fileName))
      }
      else {
        fs.delete(file.getPath,false)
      }
    }
  }

}
