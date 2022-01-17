package com.github.imcamilo.review.collection

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
    def head: Int = throw new NoSuchElementException
    def tail: IMList = throw new NoSuchElementException
    def isEmpty: Boolean = true
    def add(element: Int): IMList = new Cons(element, Empty)
    def printElements: String = ""
  }

  class Cons(hd: Int, tl: IMList) extends IMList {
     def head: Int = hd
     def tail: IMList = tl
     def isEmpty: Boolean = false
     def add(element: Int): IMList = new Cons(element, tl)
     def printElements: String =
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
    def head: Nothing = throw new NoSuchElementException
    def tail: IMList[Nothing] = throw new NoSuchElementException
    def isEmpty: Boolean = true
    def add[B >: Nothing](element: B): IMList[B] = new Cons(element, Empty)
    def printElements: String = ""
  }

  class Cons[+A](hd: A, tl: IMList[A]) extends IMList[A] {
    def head: A = hd
    def tail: IMList[A] = tl
    def isEmpty: Boolean = false
    def add[B >: A](element: B): IMList[B] = new Cons(element, tl)
    def printElements: String =
      if (tl.isEmpty) "" + hd
      else hd + " " + tl.printElements
  }

  val listOfIntegers: IMList[Int] = new Cons(1, new Cons(2, new Cons(3, new Cons(4, Empty))))
  val listOfStrings: IMList[String] = new Cons("Hello", new Cons("Scala", new Cons("World", Empty)))
  println(listOfIntegers.toString)
  println(listOfStrings.toString)
  println(listOfIntegers)

}

object IMListLatest extends App {

  abstract class IMList[+A] {
    def head: A
    def tail: IMList[A]
    def isEmpty: Boolean
    def add[B >: A](element: B): IMList[B]
    def printElements: String
    def map[B](transformer: A => B): IMList[B] ////high order functions
    def filter(test: A => Boolean): IMList[A]
    def flatMap[B](transformer: A => IMList[B]): IMList[B]
    def ++[B >: A](imList: IMList[B]): IMList[B] //concatenation
    def foreach(f: A => Unit): Unit //hofs
    def sort(compare: (A, A) => Int): IMList[A]
    def zipWith[B, C](list: IMList[B], zip: (A, B) => C): IMList[C]
    def fold[B](start: B)(operator: (B, A) => B): B
    override def toString: String = s"[$printElements]"
  }

  case object Empty extends IMList[Nothing] {
    def head: Nothing = throw new NoSuchElementException
    def tail: IMList[Nothing] = throw new NoSuchElementException
    def isEmpty: Boolean = true
    def add[B >: Nothing](element: B): IMList[B] = new Cons(element, Empty)
    def printElements: String = ""
    def map[B](transformer: Nothing => B): IMList[B] = Empty
    def filter(test: Nothing => Boolean): IMList[Nothing] = Empty
    def flatMap[B](transformer: Nothing => IMList[B]): IMList[B] = Empty
    def ++[B >: Nothing](imList: IMList[B]): IMList[B] = imList
    def foreach(f: Nothing => Unit): Unit = () //hofs
    def sort(f: (Nothing, Nothing) => Int) = Empty
    def zipWith[B, C](list: IMList[B], zip: (Nothing, B) => C): IMList[C] = {
      if (!list.isEmpty) throw new RuntimeException("list dont have the same length")
      else Empty
    }
    override def fold[B](start: B)(operator: (B, Nothing) => B): B = start
  }

  case class Cons[+A](hd: A, tl: IMList[A]) extends IMList[A] {
    def head: A = hd
    def tail: IMList[A] = tl
    def isEmpty: Boolean = false
    def add[B >: A](element: B): IMList[B] = new Cons(element, tl)
    def printElements: String =
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
    def map[B](transformer: A => B): IMList[B] =
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
    def filter(predicate: A => Boolean): IMList[A] =
      if (predicate(hd)) new Cons(hd, tl.filter(predicate))
      else tl.filter(predicate)
    /*
     * [1,2] ++ [3,4,5]
     * new Cons(1, [2] ++ [3,4,5])
     * new Cons(1, new Cons(2, Empty ++ [3,4,5]))
     * new Cons(1, new Cons(2, [3,4,5]))
     * new Cons(1, new Cons(2, new Cons(3, new Cons(4), new Cons(5))))
     */
    def ++[B >: A](imList: IMList[B]): IMList[B] = new Cons(hd, tl ++ imList)
    /*
     * [1,2].flatMap(n => [n, n+1])
     * [1,2] ++ [2].flatMap(n => [n, n+1])
     * [1,2] ++ [2,3] ++ Empty.flatMap(n => [n, n+1])
     * [1,2] ++ [2,3] ++ Empty
     * [1,2,2,3]
     */
    def flatMap[B](transformer: A => IMList[B]): IMList[B] =
      transformer(hd) ++ tl.flatMap(transformer)
    def foreach(f: A => Unit): Unit = { //hofs
      f(head)
      tail.foreach(f)
    }
    def sort(compare: (A, A) => Int): IMList[A] = {
      def insert(x: A, sortedList: IMList[A]): IMList[A] = {
        if (sortedList.isEmpty) new Cons(x, Empty)
        else if (compare(x, sortedList.head) <= 0) new Cons(x, sortedList)
        else Cons(sortedList.head, insert(x, sortedList.tail))
      }
      val sortedTail = tail.sort(compare)
      insert(head, sortedTail)
    }
    def zipWith[B, C](list: IMList[B], zip: (A, B) => C): IMList[C] =
      if (list.isEmpty) throw new RuntimeException("list dont have the same length")
      else new Cons[C](zip(head, list.head), tail.zipWith(list.tail, zip))
    /*
      [1,2,3].fold(0)(+) =
      = [2, 3].fold(1)(+) =
      = [3].fold(3)(+) =
      = [].fold(6)(+)
      = 6
     */
    def fold[B](start: B)(operator: (B, A) => B): B = {
      val newStart = operator(start, head)
      tail.fold(newStart)(operator)
    }

  }

  val listOf3Integers: IMList[Int] = new Cons(1, new Cons(2, new Cons(3, Empty)))
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
  listOfIntegers.foreach(println)
  println(listOfIntegers.sort((x, y) => y - x))
  println(listOfStrings.zipWith[Int, String](listOf3Integers, _ + "-" + _))
  println(listOfIntegers.fold(0)(_ + _))

}