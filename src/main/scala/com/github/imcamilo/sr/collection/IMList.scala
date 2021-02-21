package com.github.imcamilo.sr.collection

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

  println(listOfIntegers)

}

object IMListExpandedTest extends App {

  abstract class IMList[+A] {

    def head: A

    def tail: IMList[A]

    def isEmpty: Boolean

    def add[B >: A](element: B): IMList[B]

    def printElements: String

    override def toString: String = s"[$printElements]"

    //high order functions
    def map[B](transformer: A => B): IMList[B]

    def filter(test: A => Boolean): IMList[A]

    def flatMap[B](transformer: A => IMList[B]): IMList[B]

    //concatenation
    def ++[B >: A](imList: IMList[B]): IMList[B]

  }

  case object Empty extends IMList[Nothing] {

    override def head: Nothing = throw new NoSuchElementException

    override def tail: IMList[Nothing] = throw new NoSuchElementException

    override def isEmpty: Boolean = true

    override def add[B >: Nothing](element: B): IMList[B] = new Cons(element, Empty)

    override def printElements: String = ""

    override def map[B](transformer: Nothing => B): IMList[B] = Empty

    override def filter(test: Nothing => Boolean): IMList[Nothing] = Empty

    override def flatMap[B](transformer: Nothing => IMList[B]): IMList[B] = Empty

    override def ++[B >: Nothing](imList: IMList[B]): IMList[B] = imList

  }

  case class Cons[+A](hd: A, tl: IMList[A]) extends IMList[A] {

    override def head: A = hd

    override def tail: IMList[A] = tl

    override def isEmpty: Boolean = false

    override def add[B >: A](element: B): IMList[B] = new Cons(element, tl)

    override def printElements: String =
      if (tl.isEmpty) "" + hd
      else hd + " " + tl.printElements

    /*
     * [1,2,3,4].map(n * 2)
     * = new Cons(2, [2,3,4].map(n * 2))
     * = new Cons(2, new Cons(4, [3,4].map(n * 2)))
     * = new Cons(2, new Cons(4, new Cons(6, [4].map(n * 2))))
     * = new Cons(2, new Cons(4, new Cons(6, new Cons(8, Empty.map(n * 2)))))
     * = new Cons(2, new Cons(4, new Cons(6, new Cons(8, Empty))))
     */
    override def map[B](transformer: A => B): IMList[B] =
      new Cons(transformer(hd), tl.map(transformer))

    /*
     * [1,2,3,4].filter(n % 2 == 0)
     * [2,3,4].filter(n % 2 == 0)
     * = new Cons(2, [3,4].filter(n % 2 == 0))
     * [3,4].filter(n % 2  0)
     * [4].filter(n % 2  0)
     * = new Cons(2, new Cons(4, Empty.filter(n % 2 == 0)))
     * = new Cons(2, new Cons(4, Empty))
     */
    override def filter(predicate: A => Boolean): IMList[A] =
      if (predicate(hd)) new Cons(hd, tl.filter(predicate))
      else tl.filter(predicate)

    /*
     * [1,2] ++ [3,4,5]
     * new Cons(1, [2] ++ [3,4,5])
     * new Cons(1, new Cons(2, Empty ++ [3,4,5]))
     * new Cons(1, new Cons(2, [3,4,5]))
     * new Cons(1, new Cons(2, new Cons(3, new Cons(4), new Cons(5))))
     */
    override def ++[B >: A](imList: IMList[B]): IMList[B] = new Cons(hd, tl ++ imList)

    /*
     * [1,2].flatMap(n => [n, n+1])
     * [1,2] ++ [2].flatMap(n => [n, n+1])
     * [1,2] ++ [2,3] ++ Empty.flatMap(n => [n, n+1])
     * [1,2] ++ [2,3] ++ Empty
     * [1,2,2,3]
     */
    override def flatMap[B](transformer: A => IMList[B]): IMList[B] =
      transformer(hd) ++ tl.flatMap(transformer)

  }

  val listOfIntegers: IMList[Int] = new Cons(1, new Cons(2, new Cons(3, new Cons(4, Empty))))
  val cloneListOfIntegers: IMList[Int] = new Cons(1, new Cons(2, new Cons(3, new Cons(4, Empty))))
  val anotherListOfIntegers: IMList[Int] = new Cons(5, new Cons(6, Empty))
  val listOfStrings: IMList[String] = new Cons("Hello", new Cons("Scala", new Cons("World", Empty)))

  println(listOfIntegers.toString)
  println(listOfStrings.toString)

  println(listOfIntegers.map(_ * 2))

  println(listOfIntegers.filter(_ % 2 == 0))

  println(listOfIntegers.flatMap((a: Int) => Cons(a, Cons(a + 1, Empty))).toString)

  println(listOfIntegers == cloneListOfIntegers)

}