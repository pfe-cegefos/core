package fr.cegefos.pfe.controller

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._


object Launcher {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    val sparkSession = SparkSession.builder().appName("PFE project").master("local[2]").getOrCreate()

    //val rdd=sparkSession.read.text("D:\\Homeware\\DEV\\PFE\\src\\main\\resources")

    val df = sparkSession.read.format("CSV").option("header", true).option("sep",";").load("D:\\Homeware\\DEV\\PFE\\src\\main\\resources")

    //val df1 = df.withColumn("Games",split(col("Games"), " ").getItem(0))
    //df1.show(10)

    val df1 = df.coalesce(1)
    df1.write.partitionBy("Year").parquet("D:\\Homeware\\DEV\\PFE\\src\\main\\resources\\target")
  }
}
