package fr.cegefos.pfe.service

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

class SplitJOFile {

  def start() {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val srcPath = "D:\\Homeware\\DEV\\pfe-cegefos\\core\\Scala\\src\\main\\resources"
    val destPath = "D:\\Homeware\\DEV\\pfe-cegefos\\core\\Scala\\src\\main\\resources\\target"

    val sparkSession = SparkSession.builder().appName("PFE project").master("local[2]").getOrCreate()

    val df = sparkSession.read.format("CSV").option("header", true).option("sep", ";").load(srcPath)

    df
      .coalesce(1)
      .write
      .partitionBy("Year")
      .option("header", "true")
      .option("delimiter", ";")
      .csv(destPath)
  }
}
