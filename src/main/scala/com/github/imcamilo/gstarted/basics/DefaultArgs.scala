package com.github.imcamilo.gstarted.basics

object DefaultArgs extends App {

  def trFact(n: Int, acc: Int = 1): Int =
    if (n <= 1) acc
    else trFact(n - 1, n * acc)

  val factorial10 = trFact(10)
  def savePicture(format: String = "jpg", width: Int = 1920, height: Int = 1080) = println("saved")

  savePicture()
  savePicture(width = 200)
  savePicture(height = 300, width = 654, format = "pdf")

}
