package com.github.imcamilo.advanced

import scala.util.Try

object Sugars extends App {

  //syntax sugar #1: methods with single param
  def singleParamMethod(a: Int): String = s"$a little duck"
  val description = singleParamMethod {
    //write some complex code
    10
  }
  val aTryInstance = Try {
    throw new RuntimeException
  }
  List(1, 2, 3).map { a =>
    a + 1
  }

  //syntax sugar #2: single abstract method pattern
  //instances of traits with a single methods, can be reduced to lambdas
  trait Action {
    def act(a:Int): Int
  }
  val anInstance: Action = new Action {
    override def act(a: Int): Int = a + 1
  }
  val aFunkyInstance: Action = (a: Int) => a + 1 //magic here
  //example Runnables
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Hi, Scala")
  })
  val aSweeterThread = new Thread(() => println("Sweet, Scala"))
  abstract class AnAbstractType {
    def implemented = 42
    def f(a: Int): Unit
  }
  val anInstanceOfAbstractType = new AnAbstractType {
    override def f(a: Int): Unit = println("sweet")
  }
  val anInstanceOfAbstractType2 = (a: Int) => println("sweet") //OK too

  //syntax sugar #3: :: and #:: methods are special
  val prependedList = 2 :: List(3, 4)
  //I could think => 2.::(List(3, 4)) //but this doesn't work, because Int doesn't have a :: method.
  //So it is => List(3, 4).::(2)
  //why?
  //scala specification: the last character decides the associativity of the method
  1 :: 2 :: 3 :: List(4, 5)
  List(4, 5).::(3).::(2).::(1) //Equivalent
  class MyStream[A] {
    def -->:(a: Int): MyStream[A] = this //actual implementation
  }
  val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int] //special char finishing with : right associative

  //syntax sugar #4: multi-word method naming //or feature
  class Teen(name: String) {
    def `and then said`(g: String) = s"$name said $g"
  }
  val lilly = new Teen("Lilly")
  lilly `and then said` "Scala is great"

  //syntax sugar #5: infix types
  class Composite[A, B]
  val composite1: Composite[Int, String] = ???
  val composite2: Int Composite String = ???

  class -->[A, B]
  val towards: Int --> String = ???

  //syntax sugar #6: update() is very special, much like apply()
  val anArray = Array(1, 3, 3)
  anArray(2) = 5 //compile re writes to anArray.update(2, 5)
  //used in mutable collections
  //remember apply() AND update()

  //syntax sugar #7: setters for mutable containers
  class Mutable {
    private var internalMember: Int = 0 //private for OO encapsulation
    //if the class has two special methods, we could update internalMember easily
    def member: Int = internalMember //"getter"
    def member_=(a: Int): Unit = internalMember = a //"setter"
  }
  val aMutableContainer = new Mutable
  aMutableContainer.member = 43 //rewritten as aMutableContainer.member_=(42)

}
