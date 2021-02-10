package com.github.imcamilo.gstarted.functional

object WhatsAFunction extends App {

  //DREAM : use functions as first class elements
  //Problem: OOP

  val doubler = new MyFunction[Int, Int] {
    override def apply(elem: Int): Int = elem * 2
  }
  println(doubler(2))

  //Function types = Function[A, B] //up to 22 parameters
  val stringToIntConverter = new Function[String, Int] {
    override def apply(v1: String): Int = v1.toInt
  }
  println(stringToIntConverter("6") + 4)

  val adder = new Function2[Int, Int, Int] {
    override def apply(v1: Int, v2: Int): Int = v1 + v2
  }
  val alsoAdder: Function2[Int, Int, Int] = new Function2[Int, Int, Int] {
    override def apply(v1: Int, v2: Int): Int = v1 + v2
  }
  val alsoAdder2: (Int, Int) => Int = new Function2[Int, Int, Int] {
    override def apply(v1: Int, v2: Int): Int = v1 + v2
  }

  //Function Types - Function2[A, B, C] === (A, B) => C

  //ALL SCALA FUNCTIONS ARE OBJECTS

  //a function that takes 2 strings and concatenate them
  val concatenator: (String, String) => String = new Function2[String, String, String] {
    override def apply(v1: String, v2: String): String = v1 + v2
  }
  println(concatenator("Cam","ilo"))

  //define a functions which takes an int and return another function which takes an int and returns an int
  val superAdder: (Int) => ((Int) => Int) = new Function1[Int, Function[Int, Int]] {
    override def apply(v1: Int): Function1[Int, Int] = new Function[Int, Int] {
      override def apply(v2: Int): Int = v1 + v2
    }
  }
  val adder3 = superAdder(3)
  println(adder3(4))
  println(superAdder(3)(4)) //Curried //Can be call with multiple paramters

}

class Action0[A, B] {
  def excute(elem: A): B = ???
}

trait Action[A, B] {
  def excute(elem: A): B
}

trait MyFunction[A, B] {
  def apply(elem: A): B
}

