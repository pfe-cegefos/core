package fr.cegefos.pfe.service

import com.mongodb.spark.MongoSpark
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.bson.Document
import org.apache.spark.sql.functions.max

class MongoDBWriter(var sparkSession:SparkSession) {

  val INPUT_PATH = "src/main/resources/local/mongodb/"

  def start() {

    //val df = sparkSession.read.format("CSV").option("header", true).option("sep", ";").load(INPUT_PATH)
    val df = sparkSession.read.csv(INPUT_PATH)

    copyToMongoDB(df, "","")
  }

  /**
  Copy data from hdfs to MongoDB
    */
  def copyToMongoDB(df:Dataset[Row], src:String, dest:String): Unit ={
    val lastGames = df.agg(max("games")).first.getString(0)

    val dfLastGames = df.filter("games = '" + lastGames +  "'")
    val skipableFirstRow = dfLastGames.first()
    val dfLastGamesWithoutHead = dfLastGames.filter(row => row != skipableFirstRow)

    val lines: RDD[Row] = dfLastGamesWithoutHead.rdd

    val players = lines
                    .map(lines => lines
                                  .toString()
                                  .replace(lastGames,"").replace(",]","").replace("[","")
                                  .split(";")
                        )
                    .map(line => {
                      val doc = new Document()
                      doc.append("IDs", line(0))
                      doc.append("Name", line(1))
                      doc.append("Sex", if (line.length>2) line(2) else "")
                      doc.append("Age", if (line.length>3) line(3) else "")
                      doc.append("Height", if (line.length>4) line(4) else "")
                      doc.append("Weight",if (line.length>5) line(5) else "")
                      doc.append("Team",  if (line.length>6) line(6) else "")
                      doc.append("NOC", if (line.length>7) line(7) else "")
                      doc.append("Year", if (line.length>8) line(8) else "")
                      doc.append("Season", if (line.length>9) line(9) else "")
                      doc.append("City", if (line.length>10) line(10) else "")
                      doc.append("Sport", if (line.length>11) line(11) else "")
                      doc.append("Event", if (line.length>12) line(12) else "")
                      doc.append("Medal", if (line.length>13) line(13) else "")

                      doc
                    })

    for (va <- players) {
      println(va)
    }

    //drop entire collection
    MongoSpark.write(df.filter("Games = '0000-Summer'")).mode("overwrite").save();

    MongoSpark.save(players)

  }
}
