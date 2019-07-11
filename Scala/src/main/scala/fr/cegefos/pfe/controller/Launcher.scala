package fr.cegefos.pfe.controller

import fr.cegefos.pfe.service.{SplitJOFile,IngestJOFiles}

object Launcher {
  def main(args: Array[String]): Unit = {

    if (args.length == 0) {
      println("dude, i need at least one parameter")
    }
    val type_ = args(0)

    if (type_ == "split") {
      val splitJOFile = new SplitJOFile()
      splitJOFile.start()

    }else if (type_ == "ingestion"){
      val srcLocal = "D:\\\\Homeware\\\\DEV\\\\pfe-cegefos\\\\core\\\\Scala\\\\src\\\\main\\\\resources\\local\\JO\\done"
      //val src = "file://192.168.0.28:22/home/cloudera/JO/done/"
      //val dest = "D:\\Homeware\\DEV\\pfe-cegefos\\core\\Scala\\src\\main\\resources\\data\\raw\\JO\\public"
      //val dest = "hdfs:///192.168.0.28:8020/data/raw/JO/public"
      val srcRaw = "hdfs://192.168.2.16:8020/dev/raw/JO/input"
      val srcLake = "hdfs://192.168.2.16:8020/data/raw/JO/public"

      val ingestJOFiles = new IngestJOFiles()
      ingestJOFiles.start(srcLocal, srcRaw, srcLake)
    }
  }
}
