package fr.cegefos.pfe.controller

import fr.cegefos.pfe.service.MongoDBWriter
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object ApplicationMongoDBWriter {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    if (args.length == 0) {
      println("dude, i need at least one parameter")
    }
    else {
      val sparkConf = new SparkConf().setAppName("PFE Project - MongoDB")
                                    .setMaster("local") //TODO REMOVE FOR CLUSTER
                                    .set("spark.mongodb.input.uri", "mongodb://localhost:27017/JO.LastGames")
                                    .set("spark.mongodb.input.readPreference.name", "secondaryPreferred")
                                    .set("spark.mongodb.output.uri", "mongodb://localhost:27017/JO.LastGames")

      val sparkContext = new SparkContext(sparkConf)
      val sqlContext = new SQLContext(sparkContext)
/*
      val sqlContext = SQLContext.builder()
        .config(sparkConf)
        .setMaster("local")
        .config("spark.mongodb.input.uri", "mongodb://localhost:27017/JO.LastGames")
        .config("spark.mongodb.input.readPreference.name", "secondaryPreferred")
        .config("spark.mongodb.output.uri", "mongodb://localhost:27017/JO.LastGames")
        .getOrCreate()
*/


      val localConf = new Configuration()
      localConf.set("fs.defaultFS", "file://localhost")
      val localFS = FileSystem.get(localConf)

      //val src = args(0)
      val src = "D:\\Homeware\\DEV\\pfe-cegefos\\core\\Scala\\src\\main\\resources\\local\\mongodb" //TODO REMOVE FOR CLUSTER
      val mongoDBWriter = new MongoDBWriter(sqlContext, localFS)
      mongoDBWriter.start(src)
    }
  }
}
