package com.github.imcamilo.review.advanced

object CurriesAndPartialAppliedFunctions extends App {

  //curried functions
  val superAdded: Int => Int => Int = x => y => x + y
  val add3 = superAdded(3) //Int => Int = y => 3 + y
  println(add3(5))
  println(superAdded(3)(5)) //curried functions

  //Method!
  def curriedAdder(x: Int)(y: Int): Int = x + y //curried method
  //val add4 = curriedAdder(4) //type is required
  //Am I getting a result of curriedAdder, or am I defining a function?
  val add4: Int => Int = curriedAdder(4) //type is required
  //we cant use method in HOF unless they're transformed into function values
  //behind the scenes, it is called Lifting -> method function -> ETA-EXPANSION
  //functions != methods (JVM Limitation)
  def inc(x: Int) = x + 1 //using inc as a function
  List(1, 2, 3).map(inc) //the compiler does ETA-EXPANSION for us, and it turns the inc method into a function
  //the compiler rewrites List(1, 2, 3).map(inc) as List(1, 2, 3).map(x => int(x))

  //how can we force the compiler to do ETA-Expansion when we want?
  //Partial functions applications
  //underscore means, I tell the compiler do ETA-Expansion for me
  //and it turns curriedAdder into a function value after you've applied the first parameter list
  val add5 = curriedAdder(5) _ //Int => Int

  //usage
  val simpleAddFunction = (x:Int, y: Int) => x + y
  def simpleAddMethod = (x:Int, y: Int) => x + y
  def curriedAddMethod(x: Int)(y: Int) = x + y

  val add7 = (a: Int) => simpleAddFunction(7, a)
  def add7_2 = simpleAddFunction.curried(7)
  def add7_3 = curriedAddMethod(7) _ //PAF
  def add7_4 = curriedAddMethod(7)(_) //PAF = alternative syntax
  //alternative syntax for turning method into function values
  val add7_5 = simpleAddMethod(7, _: Int) //compiler rewrite to y => simpleAddMethod(7, y)
  val add7_6 = simpleAddFunction(7, _:Int) //from method/function to another function value

  //underscores are powerfull
  def concatenator(a: String, b: String, c: String): String = a + b + c
  val insertName = concatenator("Hello! ", _: String, " How are you?") //x => concatenator("Hello! ", x, " How are you?")
  println(insertName("Tonga"))

  val fillingTheBlanks = concatenator("What are you doing? ", _:String, _:String) //x, y => concatenator("Hello! ", x, y)
  println(fillingTheBlanks("Checking ", "Underscores"))

  /*
  1. process a list of numbers and return their string representation with differents formats
   */

  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)
  val formats = List("%4.2f", "%8.6f", "%14.12f")
  val curriedFormatter: String => Double => String = a => b => a.format(b)
  println(numbers.flatMap(n => formats.map(f => curriedFormatter(f)(n))))
  //else
  def curriedFormatter_2(a: String)(b: Double) = a.format(b)
  val simpleFormatter = curriedFormatter_2("%4.2f") _ //lift
  val seriousFormatter = curriedFormatter_2("%8.6f") _ //lift
  val preciseFormatter = curriedFormatter_2("%14.12f") _ //lift
  println(numbers.map(simpleFormatter))
  println(numbers.map(seriousFormatter))
  println(numbers.map(preciseFormatter))
  println(numbers.map(curriedFormatter_2("%14.12f"))) //compiler does sweet ETA-Expansion for us

  //difference between functions and methods
  //parameters: by_name vs 0-lambdas
  def byName(n: => Int) = n + 1
  def byFunction(f: () => Int) = f() + 1

  def simpleMethod: Int = 42
  def parenMethod(): Int = 42

  /*
  calling byName and byFunction
  - int
  - method
  - parenMethod
  - lambda
  - PAF
   */
  byName(23) //ok
  byName(simpleMethod) //ok
  byName(parenMethod())
  byName(parenMethod) //ok but beware ==>  byName(parenMethod()) //
  //byName(() => 43) //not ok
  byName((() => 42)()) //ok
  //byName(parenMethod _) //not ok

  //byFunction(56) //not ok
  //byFunction(simpleMethod) //not ok //does not eta expansion here
  byFunction(parenMethod) //compiler does eta-expansion
  byFunction(() => 35) //works
  byFunction(parenMethod _) //also works, but warning - unnecessary

}
