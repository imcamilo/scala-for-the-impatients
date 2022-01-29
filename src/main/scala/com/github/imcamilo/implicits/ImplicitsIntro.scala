package com.github.imcamilo.implicits

object ImplicitsIntro extends App {

  //how this works?
  val pair = "Camilo" -> "8766"
  val pairInt = 142 -> 8766

  case class Person(name: String) {
    def greet = s"Hi my name is $name"
  }

  implicit def fromStringToPerson(str: String): Person = Person(str)
  println("Max".greet) //println(fromStringToPerson("Max").greet)

  //class A {
  //  def greet = 2
  //}
  //implicit def fromStringToA(str: String): A = new A
  //how to use the correct one?

  //implicit parameters
  def increment(a: Int)(implicit amount: Int) = a + amount
  implicit val defaultAmount = 10
  increment(2)
  //NOT default args

}
