package com.github.imcamilo.review.basics

object SmartStrings extends App {

  val str: String = "Hi, I am playing"

  println(str.charAt(2))
  println(str.substring(4, 8))
  println(str.split(" ").toList)
  println(str.startsWith("Hi"))
  println(str.replace("Hi", "Hello"))
  println(str.toLowerCase)
  println(str.toUpperCase())
  println(str.length)

  val aNumberString = "88"
  val aNumber = aNumberString.toInt
  println('a' +: aNumberString :+ 'z')
  println(str.reverse)
  println(str.take(2))

  //Scala - Specific

  //S-interpolators
  val name = "Camilo"
  val age = 26
  val greeting = s"$name - $age"
  val greeting2 = s"$name - ${age + 1}"
  println(greeting)
  println(greeting2)

  //F-interpolators
  val speed = 1.2f
  val myth = f"$name can eat $speed%2.2f burgers per minute"
  println(myth)

  //raw-interpolator
  println(raw"this is a \n newLIne")
  val escaped = "this is a \n newLIne"
  println(raw"$escaped")

}
