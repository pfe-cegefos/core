/*package fr.cegefos.pfe.controller

import fr.cegefos.pfe.service.MongoDBWriter
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf

object Application {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    if (args.length == 0) {
      println("dude, i need at least one parameter")
    }
    else {
      val sparkConf = new SparkConf().setAppName("PFE project - MongoDB").setMaster("local")

      val sparkContext = new SparkContext(sparkConf)
      val sparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

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
*/