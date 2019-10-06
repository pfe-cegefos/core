package fr.cegefos.pfe.service

/*import org.apache.spark.sql.SparkSession
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path

class FileSpliter(var sparkSession: SparkSession, var fileSystem:FileSystem) {

  val SRC_PATH = "src\\main\\resources\\local\\input"
  val DST_PATH = "src\\main\\resources\\local\\tmp"
  val FINAL_PATH = "src\\main\\resources\\local\\output"

  def start() {

    val df = sparkSession.read.format("CSV").option("header", true).option("sep", ";").load(SRC_PATH)

    import org.apache.spark.sql.functions._
    val replace = udf {(s: String) => s.replace("\"","").replace("\\","").replace("\"","")}
    val df1 = df.withColumn("Name", replace(col("Name"))).withColumn("Team", replace(col("Team"))).withColumn("Sport", replace(col("Sport"))).withColumn("Event", replace(col("Event")))

    df1
      .coalesce(1)
      .write
      .partitionBy("Games")
      .option("header", "true")
      .option("delimiter", ";")
      .option("quote", "\"")
      .option("quoteMode", "true")
      .csv(DST_PATH)

    renaming
  }

  def renaming: Unit ={
    val files = fileSystem.listFiles(new Path(DST_PATH), true)

    while (files.hasNext()) {
      val file = files.next()

      if(file.getPath.getName.split("\\.").last == "csv") {
        val fileName = file.getPath.getParent.getName.replace("games=","").replace("%20","-")
        fileSystem.rename(file.getPath, new Path(FINAL_PATH + "//" + fileName))
      }
      else {
        fileSystem.delete(file.getPath,false)
      }
    }
  }
}
*/