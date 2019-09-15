package fr.cegefos.pfe.service

import org.apache.hadoop.fs.FileSystem
import org.apache.spark.sql.SparkSession
import org.apache.hadoop.fs.Path
import java.util.Calendar

class DatalakeWriter(var sparkSession:SparkSession, hdfsFS:FileSystem, localFS:FileSystem) {

  val arrayGames = new Array[Path](100)
  var nbGames = 0

  def start(srcLocal:String, srcRaw:String, srcLake:String) {
    copyToRaw(srcLocal, srcRaw)
    copyToApp(srcRaw, srcLake)
  }

  /**
  Copy data from Local machine to HDFS Raw space - keeping in CSV file
   */
  def copyToRaw(src:String, dest:String): Unit ={

    val versionDate = getDate
    val srcPath = new Path(src)

    val files = localFS.listFiles(srcPath, false)

    while (files.hasNext()) {
      val file = files.next()
      val name = file.getPath.getName //returns filename
      val currentPath = dest + "/version=current/" + name
      val versionPath= dest + "/version=" + versionDate + "/" + name

      //TODO Enhance this part to manage reinsertion of same file data (versioning management)
      val currentFilePath = new Path(currentPath + "/" + name + ".csv")
      val versionFilePath = new Path(versionPath + "/" + name + ".csv")

      if (hdfsFS.exists(new Path(currentPath))) {
        //move to version folder
        hdfsFS.mkdirs(new Path(versionPath))
        hdfsFS.rename(currentFilePath, versionFilePath)
      }else {
        //create path if not exists
        hdfsFS.mkdirs(new Path(currentPath))
      }

      hdfsFS.copyFromLocalFile(file.getPath, currentFilePath)

      arrayGames(nbGames) = currentFilePath
      nbGames = nbGames + 1
    }
  }

  /**
  Copy data from HDFS raw space to HDFS Lake space - transformation to Parquet file
   */
  def copyToApp(src:String, dest:String): Unit ={
    var i = 0
    while (i < nbGames) {

      val df = sparkSession.read.format("CSV").option("header", true).option("sep", ";").load(arrayGames(i).toString)

      val partitionName = arrayGames(i).getParent.getParent.getName
      val lakePath = arrayGames(i).getParent.getParent.getParent.toString.replace("raw", "app") + "/Games=" + partitionName

      val lakeDestPath =
        df
          .coalesce(1)
          .write
          .parquet(lakePath)
      i = i + 1
    }
  }

  def getDate(): String ={
    val cal = Calendar.getInstance()
    val year = cal.get(Calendar.YEAR )
    val month = cal.get(Calendar.MONTH ) + 1
    val day = cal.get(Calendar.DAY_OF_MONTH )

    year.toString + month.toString + day.toString
  }
}
