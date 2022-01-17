package com.github.imcamilo.review.basics

import scala.annotation.tailrec

object Recursion extends App {

  def factorial(n: Int): Int = {
    if (n <= 1) 1
    else {
      println("computing factorial of " + n + " - I first need factorial of " + (n - 1))
      val result = n * factorial(n - 1)
      println("computed factorial of " + n)
      result
    }
  }

  //println(factorial(10))
  //println(factorial(5000))

  /**
   * JVM USES A **CALL STACK** TO KEEP PARTIAL RESULTS SO THAT IT CAN GET BACK TO COMPUTING THE DESIRE RESULT
   * SO EACH CALL IN THE RECURSIVE FUNCTION USES WHAT WE CALL A **STACK FRAME**
   * THE TROUBLE WITH THIS APPROACH IS THAT THE JVM KEEPS ALL THE CALLS ON ITS INTERNAL STACK WHICH HAS
   * LIMITED MEMORY.
   * STACKOVERFLOW HAPPENS WHEN A RECURSION DEPTH IS TO BIG
   */

  def betterFactorial(n: Int): BigInt = {
    @tailrec
    def factorialHelper(x: Int, accumulator: BigInt): BigInt = {
      if (x <= 1) accumulator
      else factorialHelper(x - 1, x * accumulator) //TAL RECURSION = use recursive call as the LAST expression
    }
    factorialHelper(n, 1)
  }
  println(betterFactorial(5000))

  //WHEN YOU NEED LOOPS, USE TAIL RECURSION

  /**
   * 1. CONCATENATE A STRING N TIMES
   * 2. IS_PRIME FUNCTION TAIL RECURSIVE
   * 3. FIB FUNCTION
   */

  @tailrec
  def concatenateTailrec(aString: String, n: Int, accumulator: String): String =
    if (n <= 0) accumulator
    else concatenateTailrec(aString, n - 1, aString + "_" + accumulator)
  println(concatenateTailrec("hi", 23, ""))

  def isPrime(n: Int): Boolean = {
    @tailrec
    def isPrimeTailRec(t: Int, isStillPrime: Boolean): Boolean =
      if (!isStillPrime) false
      else if (t <= 1) true
      else isPrimeTailRec(t - 1, n % t != 0 && isStillPrime)
    isPrimeTailRec(n / 2, isStillPrime = true)
  }

  println(isPrime(7))
  println(isPrime(53532))

  def fibonacciSeq(n: Int): Int = {
    @tailrec
    def fiboTailRec(i: Int, last: Int, nextLast: Int): Int =
      if (i >= n) last
      else fiboTailRec(i + 1, last + nextLast, last)
    if (n <= 2) 1
    else fiboTailRec(2, 1, 1)
  }
  println(fibonacciSeq(8))

}


