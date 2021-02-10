package com.github.imcamilo.gstarted.oop

object Exceptions extends App {

  val x: String = null
  //print(x.length) //this will crash NPE

  //1. throwing
  //throw new NullPointerException
  //val aWeirdValue = throw new NullPointerException
  //throwable classes extend the Throwable class
  //Exception and Error are the major Throwable subtypes

  //2. catching
  def getInt(withExceptions: Boolean): Int =
    if (withExceptions) throw new RuntimeException("oo int for you")
    else 53

  try {
    getInt(true)
  } catch {
    case re: RuntimeException => println("caught a runtime exception")
    case np: NullPointerException => println("caught a nullpointer exception")
  } finally {
    println("final")
  }

  //everything is a expression
  val potentialFail = try {
    getInt(false)
  } catch {
    case np: NullPointerException => 31 //potentialFail will be Int instead AnyVal
  } finally {
    //dont influence the return type of this expression
    //use finally only for side effects
    println("final")
  }

  //3. how to define your own exceptions
  class MyException extends Exception
  val exception = new MyException
  //throw exception

  //OOM
  //val array = Array.ofDim(Int.MaxValue)

  //SO
  //def infite: Int = 1 + infite
  //infite


}
