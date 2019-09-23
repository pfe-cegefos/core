package fr.cegefos.pfe.service

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.hadoop.fs.FileSystem
import org.apache.spark.sql.SQLContext
import org.apache.hadoop.fs.Path

class DatalakeWriter(var sqlContext:SQLContext, hdfsFS:FileSystem, localFS:FileSystem) {

  val arrayGames = new Array[Path](100)
  val localGames = new Array[Path](100)
  var nbGames = 0

  def start(srcLocal:String, srcRaw:String, srcLake:String) {
    copyToRaw(srcLocal, srcRaw)
    copyToLake()
  }

  /**
  Copy data from Local machine to HDFS Raw space - keeping in CSV file
   */
  def copyToRaw(src:String, dest:String): Unit ={

    val dateVersionning = new SimpleDateFormat("YYYYMMdd").format(new Date)

    val srcPath = new Path(src)

    val files = localFS.listFiles(srcPath, false)

    while (files.hasNext()) {
      val file = files.next()
      val name = file.getPath.getName //returns filename
      val currentPath = dest + "/version=current/" + name
      val versionPath= dest + "/version=" + dateVersionning + "/" + name

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

      localGames(nbGames) = file.getPath
      arrayGames(nbGames) = currentFilePath
      nbGames = nbGames + 1
    }
  }

  /**
  Copy data from HDFS raw space to HDFS Lake space - transformation to Parquet file
   */
  def copyToLake(): Unit ={
    var i = 0
    while (i < nbGames) {
      val srcPath = arrayGames(i)
      val srcDonePath = new Path(localGames(i).getParent + "/" + "done")

      val df = sqlContext.read.format("CSV").option("header", true).option("delimiter", ";").load(srcPath.toString)

      val partitionName = arrayGames(i).getParent.getName
      val lakePath = arrayGames(i).getParent.getParent.getParent.toString.replace("raw", "lake") + "/games=" + partitionName

      val lakeDestPath =
        df
          .coalesce(1)
          .write
          .parquet(lakePath)

      //move data copied into Done Path: to avoid additional copy
      localFS.rename(localGames(i), srcDonePath)

      i = i + 1
    }
  }
}
