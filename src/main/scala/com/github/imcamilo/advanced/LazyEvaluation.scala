package com.github.imcamilo.advanced

object LazyEvaluation extends App {

  //lazy delays the evaluation of values
  lazy val x: Int = {
    println("Hi")
    42
  }
  println(x) //first evaluation only when you use for the first time
  println(x) //not will be evaluated again, it kepp the same value

  //example implications
  //side effects
  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }

  def simpleCondition: Boolean = false
  lazy val lazyCondition = sideEffectCondition
  println(if (simpleCondition && lazyCondition) "yes" else "no")

  //in conjunction with call by name
  def byNameMethod(n: => Int): Int = {
    //CALL BY NEED
    lazy val t = n
    t + t + t + 1
  }
  def retrieveMagicValue = {
    println("Waiting")
    Thread.sleep(1000)
    42
  }
  println(byNameMethod(retrieveMagicValue))
  //use lazy vals

  //filtering with lazy vals
  def lessThan30(a: Int): Boolean = {
    println(s"$a is less than 30?")
    a < 30
  }
  def greatherThan20(a: Int): Boolean = {
    println(s"$a is greather than 20?")
    a > 20
  }
  val numbers = List(1, 25, 40, 5, 23)
  val lt30 = numbers.filter(lessThan30) //List(1, 25, 5, 23)
  val gt20 = lt30.filter(greatherThan20) //List(25, 23)
  println(gt20)
  println

  val lt30Lazy = numbers.withFilter(lessThan30) //uses lazy values under the hood
  val gt30Lazy = lt30Lazy.withFilter(greatherThan20)
  println(gt30Lazy)
  gt30Lazy.foreach(println) //forces the evaluation

  //for-comprehension uses withFilter with guards
  for {
    a <- List(1, 2, 3) if a % 2 == 0 //use lazy vals
  } yield a + 1
  List(1, 2, 3).withFilter(_ % 2 == 0).map(_ + 1) //List[Int]

}
