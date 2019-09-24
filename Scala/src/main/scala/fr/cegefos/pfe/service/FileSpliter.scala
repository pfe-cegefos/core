/*package fr.cegefos.pfe.service

import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path

class FileSpliter(var sqlContext: SQLContext, var fileSystem:FileSystem) {

  val SRC_PATH = "src\\main\\resources\\local\\input"
  val DST_PATH = "src\\main\\resources\\local\\tmp"
  val FINAL_PATH = "src\\main\\resources\\local\\output"

  def start() {

    val df = sqlContext.read.format("CSV").option("header", true).option("delimiter", ";").load(SRC_PATH)

    //if df.count() = 0 {
    //  throw new Exception()
    //}
    df
      .coalesce(1)
      .write
      .partitionBy("Games")
      .option("header", "true")
      .option("delimiter", ";")
      .option("quote","/"")
      .csv(DST_PATH)

    renaming
  }

  def renaming: Unit ={
    val files = fileSystem.listFiles(new Path(DST_PATH), true)

    while (files.hasNext()) {
      val file = files.next()

      if(file.getPath.getName.split("\\.").last == "csv") {
        val fileName = file.getPath.getParent.getName.replace("Games=","").replace("%20","-")
        fileSystem.rename(file.getPath, new Path(FINAL_PATH + "//" + fileName))
      }
      else {
        fileSystem.delete(file.getPath,false)
      }
    }
  }

}
*/