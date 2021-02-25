package com.github.imcamilo.sr.functional

object HOFAndCurries extends App {

  val superFunction: (Int, (String, (Int => Boolean)) => Int) => (Int => Int) = null
  //higher order function
  //map, flatmap, filter

  //function that applies a function n times over a value x
  //nTimes(f, n, x)
  //nTimes(f, 3, x) = f(f(f(x))) = nTimes(f, 2, f(x)) = f(f(f(x)))
  //nTimes(f, n, x) = f(f(...f(x))) = nTimes(f, n-1, f(x)) = rec

  def nTimes(f: Int => Int, n: Int, x: Int): Int =
    if (n <= 0) x
    else nTimes(f, n - 1, f(x))

  val plusOne = (x: Int) => x + 1

  println(nTimes(plusOne, 10, 1))

  //ntb(f,n) = x => f(f(f...(x)))
  //increment10 = ntb(plusOne, 10) = x => plusOne(plusOne...(x))
  //val y = increment10(1)
  def nTimesBetter(f: Int => Int, n: Int): (Int => Int) =
    if (n <= 0) (x: Int) => x
    else (x: Int) => nTimesBetter(f, n-1)(f(x)) //nTimesBetter(f, n-1).apply(f.apply(x))

  val plus10 = nTimesBetter(plusOne, 10)
  println(plus10(1))

  //curried
  val superAdderFunction: Int => Int => Int = (x: Int) => (y: Int) => x + y
  val add3 = superAdderFunction(3) //y = 3 + y
  println(add3(10))
  println(superAdderFunction(10)(3))

  //function with multiple parameter list
  def curriedFormatter(c: String)(x: Double): String = c.format(x)
  val standarFormat: Double => String = curriedFormatter("%4.2f")
  val preciseFormat: Double => String = curriedFormatter("%10.8f")
  println(standarFormat(Math.PI))
  println(preciseFormat(Math.PI))

}
