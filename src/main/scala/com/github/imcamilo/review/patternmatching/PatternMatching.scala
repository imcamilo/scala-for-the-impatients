package com.github.imcamilo.review.patternmatching

import scala.util.Random

object PatternMatching extends App {

  //switch on steroids
  val random = new Random()
  val x = random.nextInt(10)

  val xDescription = x match {
    case 1 => "ases"
    case 2 => "tontos"
    case 3 => "trenes"
    case 4 => "cuartas"
    case 5 => "quintas"
    case 6 => "sextas"
    case _ => "what?" //_ = wildcard
  }

  println(x)
  println(xDescription)

  //1. Decompose values
  case class Person(name: String, age: Int)

  val bob = Person("Bob", 20)

  //this is able to deconstruct a Person,
  //is able to extract n and a
  val greeting = bob match {
    case Person(n, a) if a < 21 => s"Hi, my name is $n and I am $a yo, and I cant drink in the US"
    case Person(n, a) => s"Hi, my name is $n and I am $a yo"
    case _ => "I dont know who I am"
  }
  println(greeting)

  /**
   * 1. cases are matched in order
   * 2. what if no cases match? MatchError => save yourself with _ WILDCARDS
   * 3. type of the PM expression? unified type of all the types in all cases
   * 4. PM works really well with case classes
   */

  //PM on sealed hierarchies
  sealed class Animal

  case class Dog(breed: String) extends Animal

  case class Parrot(greeting: String) extends Animal

  val animal: Animal = Dog("Terra Nova")
  animal match {
    case Dog(someBreed) => println(s"Match a dog of the $someBreed breed")
  }

  //dont try to match everything
  val isEven = x % 2 == 0
  //dont do this
  val isEven2 = x match {
    case n if n % 2 == 0 => true
    case _ => false
  }

  val isEvenCond = if (x % 2 == 0) true else false
  val isEvenCondOk = x % 2 == 0

  //given
  trait Expr
  case class Number(n: Int) extends Expr
  case class Sum(e1: Expr, e2: Expr) extends Expr
  case class Product(e1: Expr, e2: Expr) extends Expr

  /*
    write a simple function using pm
    takes and Expr return human readable form
    Sum(Number(2), Number(3)) => 2 + 3
    Sum(Number(2), Number(3), Number(4)) => 2 + 3 + 4
    Prod(Sum(Number(2), Number(1)), Number(3)) => (2 + 1) * 3
    Sum(Prod(Number(2), Number(1)), Number(3)) => (2 * 1) + 3
   */

  def show(e: Expr): String = e match {
    case Number(n) => s"$n"
    case Sum(a, b) => show(a) + " + " + show(b) //s"$a + $b"
    case Product(a, b) => {
      def maybeShowParentheses(exp: Expr) = exp match {
        case Product(_, _) => show(exp)
        case Number(_) => show(exp)
        case _ => "(" + show(exp) + ")"
      }

      maybeShowParentheses(a) + " * " + maybeShowParentheses(b)
    }
  }

  println(show(Sum(Number(13), Number(13))))
  println(show(Sum(Sum(Number(2), Number(3)), Number(4))))
  println(show(Product(Sum(Number(2), Number(1)), Number(3))))
  println(show(Product(Sum(Number(2), Number(1)), Sum(Number(3), Number(3)))))
  println(show(Sum(Product(Number(2), Number(1)), Number(3))))
  println(show(Sum(Product(Number(2), Number(1)), Product(Number(3), Number(3)))))

}
