package com.github.imcamilo.gstarted.oop

object IMListIntTest extends App {

  abstract class IMList {
    def head: Int

    def tail: IMList

    def isEmpty: Boolean

    def add(element: Int): IMList

    def printElements: String

    override def toString: String = s"[$printElements]"
  }

  object Empty extends IMList {
    override def head: Int = throw new NoSuchElementException

    override def tail: IMList = throw new NoSuchElementException

    override def isEmpty: Boolean = true

    override def add(element: Int): IMList = new Cons(element, Empty)

    override def printElements: String = ""
  }

  class Cons(hd: Int, tl: IMList) extends IMList {
    override def head: Int = hd

    override def tail: IMList = tl

    override def isEmpty: Boolean = false

    override def add(element: Int): IMList = new Cons(element, tl)

    override def printElements: String =
      if (tl.isEmpty) "" + hd
      else hd + " " + tl.printElements
  }

  val imList = new Cons(1, Empty)
  println(imList.head)
  val imList2 = new Cons(1, new Cons(2, new Cons(3, Empty)))
  println(imList2.tail.head)
  println(imList2.add(4).head)
  println(imList2.isEmpty)
  //polymorphic call
  println(imList2.toString)

}

object IMListGenericsTest extends App {

  abstract class IMList[+A] {
    def head: A

    def tail: IMList[A]

    def isEmpty: Boolean

    def add[B >: A](element: B): IMList[B]

    def printElements: String

    override def toString: String = s"[$printElements]"
  }

  object Empty extends IMList[Nothing] {
    override def head: Nothing = throw new NoSuchElementException

    override def tail: IMList[Nothing] = throw new NoSuchElementException

    override def isEmpty: Boolean = true

    override def add[B >: Nothing](element: B): IMList[B] = new Cons(element, Empty)

    override def printElements: String = ""
  }

  class Cons[+A](hd: A, tl: IMList[A]) extends IMList[A] {
    override def head: A = hd

    override def tail: IMList[A] = tl

    override def isEmpty: Boolean = false

    override def add[B >: A](element: B): IMList[B] = new Cons(element, tl)

    override def printElements: String =
      if (tl.isEmpty) "" + hd
      else hd + " " + tl.printElements
  }

  val listOfIntegers: IMList[Int] = new Cons(1, new Cons(2, new Cons(3, new Cons(4, Empty))))
  val listOfStrings: IMList[String] = new Cons("Hello", new Cons("Scala", new Cons("World", Empty)))
  println(listOfIntegers.toString)
  println(listOfStrings.toString)

}

object IMListExpandedTest extends App {

  trait MyPredicate[-T] {
    def test(value: T): Boolean
  }

  trait MyTransformer[-A, B] {
    def transformer(input: A): B
  }

  abstract class IMList[+A] {
    def head: A

    def tail: IMList[A]

    def isEmpty: Boolean

    def add[B >: A](element: B): IMList[B]

    def printElements: String

    override def toString: String = s"[$printElements]"

    def map[B](transformer: MyTransformer[A, B]): IMList[B]

    def flatMap[B](transformer: MyTransformer[A, IMList[B]]): IMList[B]

    def filter(test: MyPredicate[A]): IMList[A]
  }

  object Empty extends IMList[Nothing] {
    override def head: Nothing = throw new NoSuchElementException

    override def tail: IMList[Nothing] = throw new NoSuchElementException

    override def isEmpty: Boolean = true

    override def add[B >: Nothing](element: B): IMList[B] = new Cons(element, Empty)

    override def printElements: String = ""

    override def map[B](transformer: MyTransformer[Nothing, B]): IMList[B] = Empty

    override def flatMap[B](transformer: MyTransformer[Nothing, IMList[B]]): IMList[B] = Empty

    override def filter(test: MyPredicate[Nothing]): IMList[Nothing] = Empty
  }

  class Cons[+A](hd: A, tl: IMList[A]) extends IMList[A] {
    override def head: A = hd

    override def tail: IMList[A] = tl

    override def isEmpty: Boolean = false

    override def add[B >: A](element: B): IMList[B] = new Cons(element, tl)

    override def printElements: String =
      if (tl.isEmpty) "" + hd
      else hd + " " + tl.printElements

    override def filter(predicate: MyPredicate[A]): IMList[A] =
      if (predicate.test(hd)) new Cons(hd, tl.filter(predicate))
      else tl.filter(predicate)

    override def map[B](transformer: MyTransformer[A, B]): IMList[B] = ???

    override def flatMap[B](transformer: MyTransformer[A, IMList[B]]): IMList[B] = ???
  }


}