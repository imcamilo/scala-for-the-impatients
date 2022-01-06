package com.github.imcamilo.advanced

object PatternMatching extends App {

  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"the only element is $head") //infix pattern
    //case ::(head, Nil) => println(s"the only element is $head") the same!
    case _ =>
  }

  /**
   * only structures available for pattern matching are:
   * 1. constants
   * 2. wildcards
   * 3. case classes
   * 4. tupes
   * 5. some special magic like above
   */

  //for some reasons you can't use a case class, so:
  //how can you make it compatible with PM
  class Person(val name: String, val age: Int)

  //start with companion
  object Person {
    //returning an Option with a single value or a tuple
    def unapply(p: Person): Option[(String, Int)] =
      if (p.age < 21) None
      else Some((p.name, p.age))

    def unapply(age: Int): Option[String] = Some(if (age < 21) "minor" else "major")
  }

  val bob = new Person("Bob", 44)
  val greeting = bob match {
    case Person(n, a) => s"Hi my name is $n and my age is $a yo"
  }
  println(greeting)

  val legalStatus = bob.age match {
    case Person(status) => s"my legal status: $status"
  }
  println(legalStatus)

  //
  val n: Int = 42
  val mathProperty = n match {
    case x if x < 10 => "single digit"
    case x if x % 2 == 0 => "even number"
    case _ => "no property"
  }
  println(mathProperty)

  //could be use extractors
  object singleDigit {
    def unapply(arg: Int): Option[Boolean] = {
      if (arg < 10) Some(true)
      else None
    }
  }

  object even {
    def unapply(arg: Int): Option[Boolean] = {
      if (arg % 2 == 0) Some(false) //always match - reduce it
      else None
    }
  }

  val m: Int = 42
  val mathProperty2 = m match {
    case singleDigit(a) => "single digit"
    case even(_) => "even number"
    case _ => "no property"
  }
  println(mathProperty2)

  //better
  object betterSingleDigit {
    def unapply(arg: Int): Boolean = arg < 10
  }

  object betterEven {
    def unapply(arg: Int): Boolean = arg % 2 == 0
  }

  val b: Int = 3
  val mathProperty3 = b match {
    case betterSingleDigit() => "single digit"
    case betterEven() => "even number"
    case _ => "no property"
  }
  println(mathProperty3)

  //INFIX PATTERNS
  //this only works when you have 2 things in a pattern
  case class Or[A, B](a: A, b: B) //this would be an Either
  val either = Or(2, "two")
  val humanDescription = either match {
    //case Or(number, string) => s"$number is written as $string"
    case number Or string => s"$number is written as $string"
  }
  println(humanDescription)

  //DECOMPOSING SEQUENCES
  val vararg = numbers match {
    case List(1, _*) => "starting with 1"
  }

  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }

  case object Empty extends MyList[Nothing]

  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _) //:: doesnt work with sequences so +:
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  val decomposed = myList match {
    case MyList(1, 2, _*) => "starting with 1, 2"
    case _ => "something else"
  }
  println(decomposed)

  //CUSTOM RETURN TYPES FOR UNAPPLY
  //always needs two methods = isEmpty: Boolean, get: Something

  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }
  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      override def isEmpty: Boolean = false
      override def get: String = person.name
    }
  }

  println(bob match {
    case PersonWrapper(name) => s"The person's name is $name"
    case _ => "an Alien"
  })

}
