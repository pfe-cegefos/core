package fr.cegefos.pfe.service

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import java.util.{Calendar, Date}

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

class IngestJOFiles {

  val arrayGames = new Array[Path](100)
  var nbGames = 0

  def start(srcLocal:String, srcRaw:String, srcLake:String) {
    Logger.getLogger("org").setLevel(Level.ERROR)

    copyToRaw(srcLocal, srcRaw)
    copyToApp(srcRaw, srcLake)
  }
/*
  def copyToRaw(src:String, dest:String): Unit ={
    val hadoopConf = new Configuration()
    hadoopConf.addResource("conf/core-site.xml")
    val hdfs = FileSystem.get(hadoopConf)

    val srcPath = new Path(src)
    val files = hdfs.listFiles(srcPath, false)

    val versionDate = getDate
    println(versionDate)

    while (files.hasNext()) {
      val file = files.next()

      val name = file.getPath.getName
      val currentFolder = dest + "/" + name.replace("jo-","") + "/current/"
      val versionFolder = dest + "/" + name.replace("jo-","") + "/" + versionDate

      //TODO Enhance this part to manage reinsertion of same file data (versionning management)
      val destPath = new Path(currentFolder + "/" + name + ".csv")

      if (hdfs.exists(new Path(currentFolder))){
        //move to version folder
        val versionPath = new Path(versionFolder + "/" + name + ".csv")
        hdfs.rename(destPath, versionPath)
      }

      hdfs.copyFromLocalFile(file.getPath, destPath)

      arrayGames(nbGames) = destPath
      nbGames = nbGames + 1
    }
  }
  */
  def copyToRaw(src:String, dest:String): Unit ={

    val hadoopConf = new Configuration()
    hadoopConf.addResource("src/main/conf/core-site.xml")
    hadoopConf.set("fs.defaultFS", "hdfs://192.168.2.16:8020")
    val hdfs = FileSystem.get(hadoopConf)

    val srcPath = new Path(src)
/*
    val configuration = new Configuration()
    configuration.set("fs.defaultFS", "hdfs://172.20.10.3:8020")
    val fs = FileSystem.get(configuration)

    fs.copyFromLocalFile(new Path(src), new Path(dest))
*/

    val files = hdfs.listFiles(srcPath, false)

    val versionDate = getDate
    println(versionDate)

    while (files.hasNext()) {
      val file = files.next()

      val name = file.getPath.getName
      val currentFolder = dest + "/" + name.replace("jo-","") + "/current/"
      val versionFolder = dest + "/" + name.replace("jo-","") + "/" + versionDate

      //TODO Enhance this part to manage reinsertion of same file data (versionning management)
      val destPath = new Path(currentFolder + "/" + name + ".csv")

      if (hdfs.exists(new Path(currentFolder))){
        //move to version folder
        val versionPath = new Path(versionFolder + "/" + name + ".csv")
        hdfs.rename(destPath, versionPath)
      }

      hdfs.copyFromLocalFile(file.getPath, destPath)

      arrayGames(nbGames) = destPath
      nbGames = nbGames + 1
    }
  }

  def copyToApp(src:String, dest:String): Unit ={
    val hadoopConf = new Configuration()
    val hdfs = FileSystem.get(hadoopConf)
    val fs = FileSystem.get(hadoopConf)

    val sparkSession = SparkSession.builder().appName("PFE project copy To App").master("local[2]").getOrCreate()

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
    val date = cal.get(Calendar.DATE )
    val year = cal.get(Calendar.YEAR )
    val month = cal.get(Calendar.MONTH ) + 1
    val day = cal.get(Calendar.DAY_OF_MONTH )

    year.toString + month.toString + day.toString
  }
}
