package fr.cegefos.pfe.controller

import fr.cegefos.pfe.service.SplitJOFile


object Launcher {
  def main(args: Array[String]): Unit = {
    val splitJOFile = new SplitJOFile()

    splitJOFile.start()

  }
}
