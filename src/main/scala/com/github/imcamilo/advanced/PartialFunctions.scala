package com.github.imcamilo.advanced

object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1 //Function[Int, Int] === Int => Int

  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }
  //{1, 2, 5} => Int

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  } //partial function value

  println(aPartialFunction(2))
  //println(aPartialFunction(54231)) //MatchError

  //PF Utilities
  println(aPartialFunction.isDefinedAt(42))

  val lifted = aPartialFunction.lift //Int => Option[Int] //partial function to total function
  println(lifted(2))
  println(lifted(53))

  val pfChained = aPartialFunction.orElse[Int, Int] {
    case 45 => 654
  }
  println(pfChained(2))
  println(pfChained(45))

  //PF extends normal functions
  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  //HOF accepts Partial Functions as well
  val aMapList = List(1, 2, 3).map {
    case 1 => 42
    case 2 => 69
    case 3 => 1000
    //case 5 => 1000
  }
  println(aMapList)


  /*
  NOTE:
  partial functions can only have one parameter type
  */

  /**
   * excercise:
   * construct a PF instance yourself (anonymous class)
   * dumb chat bot as PF
   */
  val myFussyPF: PartialFunction[String, String] = new PartialFunction[String, String] {
    override def apply(x: String): String = x match {
      case "Hello" => "Hi, my name is HAL 9000"
      case "Bye" => "I'm afraid Camilo."
    }
    override def isDefinedAt(x: String): Boolean =
      x == "Hello" || x == "Bye"
  }

  val myChatBot: PartialFunction[String, String] = {
    case "Hello" => "Hi, my name is HAL 9000"
    case "Bye" => "I'm afraid Camilo."
  }

  //scala.io.Source.stdin.getLines().foreach(l => println(myChatBot(l)))
  scala.io.Source.stdin.getLines().map(myChatBot).foreach(println)

}
