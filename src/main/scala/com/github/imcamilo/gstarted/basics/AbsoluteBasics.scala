package com.github.imcamilo.gstarted.basics

class TypesAndVariables {

  //val x: Int = 144 || val x = 144

  val simpleString: String = "scala"
  val anotherString: String = "lang"
  val aBoolean: Boolean = false
  val aChar: Char = 'a'
  val aShort: Short = 1213
  val aInt: Int = 121312123
  val aLong: Long = 12131212345113L
  val aFloat: Float = 2.3f
  val aDouble: Double = 3.14

  //variables has side effects
  var aVariable: Int = 4
  aVariable = 54

}

object Expressions extends App {

  var aVariable: Int = 4

  val a = 1 + 2 //EXPRESSION

  println(a)

  println(2 + 3 * 4)
  //+ - * / & | << >> >>> (right shift wth zero extension)

  println(1 == 3)
  //== != < <= => >

  println(!(1 == 1))
  //! && ||

  var sec = 3
  sec += 3 //also works with -= /= *= ....side effect
  println(sec)

  //instruction (DO) vs expressions (VALUE)
  val aCondition = true

  val aConditionValue = if (aCondition) 5 else 3 //IF EXPRESSION
  println(aConditionValue)

  //DONT  use this
  var count = 0
  while (count < 10) {
    println(count)
    count += 1
  }
  //DONT WRITE THIS AGAIN

  //EVERYTHING IN SCALA IS AN EXPRESSION (!class, val o package)

  val aWeirdValue: Unit = (aVariable = 3) //Unit === Void
  println(aWeirdValue)

  //side effects: println(), whiles, reasigning

  //code blocks, special kind of expressions
  //the value of code block is the las expression
  val aCodeBlock = {
    val y = 1
    val x = 3
    if (y > x) "valid y" else "valid x"
  }

}

object Functions extends App {

  def aFunction(a: String, b: Int): String = a + " " + b

  println(aFunction("hi", 4))

  def aParameterLessFunction(): Int = 342

  println(aParameterLessFunction())
  println(aParameterLessFunction)

  def aRepeatedFunctioin(aString: String, n: Int): String = {
    if (n == 1) aString
    else aString + aRepeatedFunctioin(aString, n - 1)
  }

  println(aRepeatedFunctioin("rec", 5))
  //WHEN YOU NEED LOOPS, USE RECURSION

  def aFunctionWithSideEffects(a: String): Unit = println(a)

  aFunctionWithSideEffects("logging side effects")

  //with an auxiliary function inside
  def aBigFunction(n: Int): Int = {
    def aSmallerFunction(a: Int, b: Int): Int = a + b

    aSmallerFunction(n, n + 2)
  }

  def factorial(n: Int): Int =
    if (n <= 0) 1
    else n * factorial(n - 1)

  println("factorial " + factorial(5))

  def fibonacciSeq(n: Int): Int =
    if (n <= 2) 1
    else fibonacciSeq(n - 1) + fibonacciSeq(n - 2)

  //1 1 2 3 5 8 13 21
  println("fibonacci " + fibonacciSeq(8))

  def isPrime(n: Int): Boolean = {
    @scala.annotation.tailrec
    def isPrimeUntil(t: Int): Boolean =
      if (t <= 1) true
      else n % t != 0 && isPrimeUntil(t - 1)

    isPrimeUntil(n / 2)
  }

  println("is prime: " + isPrime(37))
  println("is prime: " + isPrime(2003))
  println("is prime: " + isPrime(37 * 17))

}