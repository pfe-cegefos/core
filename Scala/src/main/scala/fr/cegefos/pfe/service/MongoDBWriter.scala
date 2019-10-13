package fr.cegefos.pfe.service

import com.mongodb.spark.MongoSpark
import org.apache.hadoop.fs.{FileStatus, FileSystem, Path}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SQLContext}
import org.bson.Document

class MongoDBWriter(var sqlContext:SQLContext, var localFS:FileSystem) {

  //Copy data from hdfs to MongoDB
  def start(inputPath: String) {
    //determine last games
    val status: Array[FileStatus] = localFS.listStatus(new Path(inputPath))

    val srcPath = status.foldLeft(new FileStatus())((a: FileStatus, b: FileStatus) =>
      if (a.toString().compareTo(b.toString) > 0) a else b
    )

    val df: DataFrame = sqlContext.read.format("com.databricks.spark.csv").option("header", "true").option("quoteMode", "NONE").option("delimiter", ";").load(srcPath.getPath.toString)

    val lines: RDD[Row] = df.rdd
    lines.mapPartitionsWithIndex {
      (idx, iter) => if (idx == 0) iter.drop(1) else iter
    }

    val players: RDD[Document] = lines
      .map(line => line
        .toString()
        .replace(",]", "").replace("[", "")
        .split(",")
      )
      .map(line => {
        val doc = new Document()
        doc.append("ID", line(0))
        doc.append("Name", line(1))
        doc.append("Sex", if (line.length > 2) line(2) else "")
        doc.append("Age", if (line.length > 3) line(3) else "")
        doc.append("Height", if (line.length > 4) line(4) else "")
        doc.append("Weight", if (line.length > 5) line(5) else "")
        doc.append("Team", if (line.length > 6) line(6) else "")
        doc.append("NOC", if (line.length > 7) line(7) else "")
        doc.append("Year", if (line.length > 8) line(8) else "")
        doc.append("Season", if (line.length > 9) line(9) else "")
        doc.append("City", if (line.length > 10) line(10) else "")
        doc.append("Sport", if (line.length > 11) line(11) else "")
        doc.append("Event", if (line.length > 12) line(12) else "")
        doc.append("Medal", if (line.length > 13) line(13) else "")

        doc
      })

    //drop entire collection
    MongoSpark.write(df.filter("ID=-1")).mode("overwrite").save()

    MongoSpark.save(players)
  }
}
