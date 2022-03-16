package com.github.imcamilo.review.readingfiles

import org.apache.commons.io.FileUtils

import java.io.File
import java.nio.charset.Charset
import java.util.Scanner

object ReadingFiles extends App {

  val filePath = "src/main/resources/reader/index.html"

  // VERSION 1 - The Java Way
  val file: File = new File(filePath)
  val scanner = new Scanner(file)
  while (scanner.hasNextLine) {
    val line = scanner.nextLine()
    //do something
    //println(line)
  }

  // VERSION 2 - The Java Way with Cheats
  val fileContent = FileUtils.readLines(file, "UTF-8") //Java List of Strings
  //fileContent.forEach(println)

  // VERSION 3 - The Scala Way
  import scala.io.Source
  //this iterator is not fully loaded in memory
  val scalaFileContent: Iterator[String] = Source.fromFile(file).getLines()
  //scalaFileContent.foreach(println)
  //close after

  // VERSION 4 - open(path).read
  def open(path: String) = new File(path)
  implicit class ReachFile(file: File) {
    def read(): Iterator[String] = Source.fromFile(file).getLines
  }
  val readLikeABoss = open(filePath).read() //new RichFile(open(filePath)).read()
  readLikeABoss.foreach(println)
  //close after

}
