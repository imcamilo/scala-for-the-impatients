package com.github.imcamilo.gstarted.basics

object Recursion extends App {

  def factorial(n: Int): Int =
    if (n <= 1) 1
    else {
      println("computing factorial of " + n + " - I first need factorial of " + (n - 1))
      val result = n * factorial(n - 1)
      println("computed factorial of " + n)
      result
    }

  //println(factorial(10))
  //println(factorial(5000))

  /**
   * JVM USES A **CALL STACK** TO KEEP PARTIAL RESULTS SO THAT IT CAN GET BACK TO COMPUTING THE DESIRE RESULT
   * SO EACH CALL IN THE RECURSIVE FUNCTION USES WHAT WE CALL A **STACK FRAME**
   * THE TROUBLE WITH THIS APPROACH IS THAT THE JVM KEEPS ALL THE CALLS ON ITS INTERNAL STACK WHICH HAS
   * LIMITED MEMORY.
   */

  /**
   * STACKOVERFLOW HAPPENS WHEN A RECURSION DEPTH IS TO BIG
   */

  def betterFactorial(n: Int): BigInt = {
    @scala.annotation.tailrec
    def factorialHelper(n: Int, accumulator: BigInt): BigInt =
      if (n <= 1) accumulator
      else factorialHelper(n - 1, n * accumulator) //TAL RECURSION = use recursive call as the LAST expression

    factorialHelper(n, 1)
  }

  println(betterFactorial(5000))
}
