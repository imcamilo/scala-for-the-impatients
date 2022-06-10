package com.github.imcamilo.review.basics

import scala.concurrent.Future
import scala.util.Try

object CallByNameTricks {

  def byValueFunction(x: Int): Int = 8766
  byValueFunction(4 + 10) // 4 + 10 // you call byValueFunction(14)

  def byNameFunction(x: =>Int): Int = x + 87
  byNameFunction(4 + 10) // 4 + 10 is passed LITERALLY

  // trick #1 -  reevaluation
  def byValuePrint(x: Long): Unit = {
    println(x)
    println(x)
  }
  def byNamePrint(x: =>Long): Unit = {
    println(x)
    println(x)
  }

  // trick #2 - call by need
  abstract class MyList[+A] {
    def head: A
    def tail: MyList[A]
  }
  // infinite collections - LazyList
  class NonEmptyList[+A](h: =>A, t: =>MyList[A]) extends MyList[A] {
    override lazy val head: A = h
    override lazy val tail: MyList[A] = t
  }

  // trick #3 - hold the door
  val attempt: Try[Int] = Try { // seem like part of the language
    throw new NullPointerException
  }
  import scala.concurrent.ExecutionContext.Implicits.global
  val aFuture: Future[Int] = Future {
    8766
  }

  def main(args: Array[String]): Unit = {
    byValuePrint(System.nanoTime())
    byNamePrint(System.nanoTime())
  }

}
