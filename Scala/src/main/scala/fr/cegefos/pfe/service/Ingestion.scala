package fr.cegefos.pfe.service

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.hadoop.fs.FileSystem
import org.apache.spark.sql.SQLContext
import org.apache.hadoop.fs.Path

class Ingestion(var sqlContext:SQLContext, hdfsFS:FileSystem, localFS:FileSystem) {

  val arrayGames = new Array[Path](100)
  val localGames = new Array[Path](100)
  var nbGames = 0

  def start(srcLocal:String, srcRaw:String, srcLake:String) {
    val dateVersionning = new SimpleDateFormat("YYYYMMdd").format(new Date)
    val srcPath = new Path(srcLocal)

    val files = localFS.listFiles(srcPath, false)

    while (files.hasNext()) {
      val file = files.next()
      val name = file.getPath.getName.replace("Games","games") //returns filename
      val currentPath = srcRaw + "/version=current/" + name
      val versionPath = srcRaw + "/version=" + dateVersionning + "/" + name

      //versioning management
      val currentFilePath = new Path(currentPath + "/" + name + ".csv")
      val versionFilePath = new Path(versionPath + "/" + name + ".csv")

      if (hdfsFS.exists(new Path(currentPath))) {
        //move to version folder
        hdfsFS.mkdirs(new Path(versionPath))
        hdfsFS.rename(currentFilePath, versionFilePath)
        //moving file from tempo zone to raw
        hdfsFS.rename(file.getPath, currentFilePath)
      }else {
        //create path if not exists
        hdfsFS.mkdirs(new Path(currentPath))
        //moving file from tempo zone to raw
        hdfsFS.rename(file.getPath, currentFilePath)
      }

      val df = sqlContext.read.format("com.databricks.spark.csv").option("header", "true").option("quoteMode","NONE").option("delimiter", ";").load(currentFilePath.toString)

      val partitionName = currentFilePath.getParent.getName
      val lakePath = currentFilePath.getParent.getParent.getParent.toString.replace("raw", "lake") + "/" + partitionName

      val lakeDestPath =
        df
          .coalesce(1)
          .write
          .parquet(lakePath)
/*
      //move data copied into Done Path: to avoid additional copy
      val srcDonePath = new Path(file.getPath.toString.replace("input", "done"))
      if (!localFS.exists(srcDonePath.getParent)) {
        //create path if not exists
        localFS.mkdirs(srcDonePath.getParent)
      }

      localFS.rename(file.getPath, srcDonePath)

 */
    }
  }
}
