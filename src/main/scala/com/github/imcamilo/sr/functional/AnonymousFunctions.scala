package com.github.imcamilo.sr.functional

object AnonymousFunctions extends App {

  //anonymous function or Lambda
  val doubler1 = (x: Int) => x * 2
  val doubler2: Int => Int = x => x * 2

  //multi params in lambda
  val adder1 = (a: Int, b: Int) => a + b
  val adder2: (Int, Int) => Int = (a, b) => a + b

  //no params
  val justDoSomething1 = () => 3
  val justDoSomething2: () => Int = () => 3
  //careful
  println(justDoSomething1) //function itself
  println(justDoSomething2()) //call -> apply

  //curly braces
  val stringToInt = { (str: String) => str.toInt }

  //syntactic sugar
  val niceIncrementer1: Int => Int = x => x + 1
  println(niceIncrementer1(132))
  val niceIncrementer2: Int => Int = _ + 1 //equivalent to x => x + 1
  println(niceIncrementer2(132))

  val niceAdder0: (Int, Int) => Int = (a: Int, b: Int) => a + b
  val niceAdder1: (Int, Int) => Int = (a: Int, b: Int) => a + b
  val niceAdder2: (Int, Int) => Int = _ + _ //equivalent (a, b) => a + b

  //curry with lambda
  val special1: Int => Int => Int = (a: Int) => (b: Int) => a + b
  val special2: Int => Int => Int = a => b => a + b
  println(special1(1)(53))
  println(special2(1)(53))


}
