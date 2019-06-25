package fr.cegefos.pfe.service
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path

import org.apache.commons.io.IOUtils

class IngestJOFiles {

  val hadoopconf = new Configuration()
  val fs = FileSystem.get(hadoopconf)

  //Create output stream to HDFS file
  val outFileStream = fs.create(new Path("hedf://<namenode>:<port>/<filename>))

  //Create input stream from local file
  val inStream = fs.open(new Path("file://<input_file>"))

  IOUtils.copy(inStream, outFileStream)

  //Close both files
  inStream.close()
  outFileStream.close()
}
