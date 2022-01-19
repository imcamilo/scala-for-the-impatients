package com.github.imcamilo.advanced

import scala.annotation.tailrec

/*
  lazily evaluated, sigly linked stream of elements,
  naturals = IMStream.from(1)(x => x + 1) = stream of natural numbers (potentially infinite!)
  naturals.take(100).foreach(println) //lazily evaluated stream of the first 100 naturals (finite stream)
  naturals.foreach(println) //will crash - infinite
  naturals.map(_ * 2) //stream of all even numbers (potentially infinite!)
*/
abstract class IMStream[+A] {
  def isEmpty: Boolean
  def head: A
  def tail: IMStream[A]
  def #::[B >: A](element: B): IMStream[B]
  def ++[B >: A](anotherStream: => IMStream[B]): IMStream[B]
  def foreach(f: A => Unit)
  def map[B](f: A => B): IMStream[B]
  def flatMap[B](f: A => IMStream[B]): IMStream[B]
  def filter(predicate: A => Boolean): IMStream[A]
  def take(a: Int): IMStream[A] //take the first element out of this stream

  /*
  [1 2 3].toList([]) =
  [2 3].toList([1]) =
  [3].toList([2 1]) =
  [].toList([3 2 1]) =
  = [1 2 3]
   */
  @tailrec
  final def toList[B >: A](acc: List[B] = Nil): List[B] =
    if (isEmpty) acc.reverse
    else tail.toList(head :: acc)
}

object EmptyStream extends IMStream[Nothing] {
  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException
  def tail: IMStream[Nothing] = throw new NoSuchElementException
  def #::[B >: Nothing](element: B): IMStream[B] = new Cons(element, this)
  def ++[B >: Nothing](anotherStream: => IMStream[B]): IMStream[B] = anotherStream
  def foreach(f: Nothing => Unit): Unit = ()
  def map[B](f: Nothing => B): IMStream[B] = this
  def flatMap[B](f: Nothing => IMStream[B]): IMStream[B] = this
  def filter(predicate: Nothing => Boolean): IMStream[Nothing] = this
  def take(a: Int): IMStream[Nothing] = this
}

class Cons[+A](hd: A, tl: => IMStream[A]) extends IMStream[A] {
  def isEmpty: Boolean = false
  override val head: A = hd
  override lazy val tail: IMStream[A] = tl //call by need
  /*
  val s = new Cons(1, EmptyStream)
  val prepend = 1 #:: s = new Cons(1, s)
   */
  def #::[B >: A](element: B): IMStream[B] = new Cons(element, this)
  def ++[B >: A](anotherStream: => IMStream[B]): IMStream[B] = new Cons(head, tail ++ anotherStream)
  def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }
  /*
  val s = new Cons(1, ?)
  mapped = s.map(_ + 1) = new Cons(2, s.tail.map(_ + 1))
    ... mapped.tail (would force the evaluation)
   */
  def map[B](f: A => B): IMStream[B] = new Cons(f(head), tail.map(f)) //preserves lazy evaluation
  def flatMap[B](f: A => IMStream[B]): IMStream[B] = f(head) ++ tail.flatMap(f)
  def filter(predicate: A => Boolean): IMStream[A] =
    if (predicate(head)) new Cons(head, tail.filter(predicate))
    else tail.filter(predicate) //preserve lazy eval!
  def take(a: Int): IMStream[A] = {
    if (a <= 0) EmptyStream
    else if (a == 1) new Cons(head, EmptyStream)
    else new Cons(head, tail.take(a - 1))
  }
}

object IMStream {
  def from[A](start: A)(generator: A => A): IMStream[A] =
    new Cons[A](start, IMStream.from(generator(start))(generator)) //wtf
}

object StreamPlayground extends App {
  val naturals = IMStream.from(1)(_ + 1)
  println(naturals.head)
  println(naturals.tail.head)
  println(naturals.tail.tail.head)

  val startFrom0 = 0 #:: naturals //naturals.#::(0)
  println(startFrom0.head)

  startFrom0.take(10000).foreach(println)

  println(startFrom0.map(_ * 2).take(100).toList())
  println(startFrom0.flatMap(a => new Cons(a, new Cons(a + 1, EmptyStream))).take(10).toList())
  println(startFrom0.filter(_ < 10).take(10).take(20).toList())

  //stream of fibbonacci numbers
  //stream of prime numbers with Eratosthenes' sieve, filter divisibles by 2 and 3

  /*
  [ first, [ ...
  [ first, fibonacci(second, first + second) ]
   */
  def fibonacci(first: BigInt, second: BigInt): IMStream[BigInt] =
    new Cons[BigInt](first, fibonacci(second, first + second))

  println(fibonacci(1, 1).take(100).toList())

  def eratosthenes(numbers: IMStream[Int]): IMStream[Int] =
    if (numbers.isEmpty) numbers
    else new Cons(numbers.head, eratosthenes(numbers.tail.filter(_ % numbers.head != 0)))

  println(eratosthenes(IMStream.from(2)(_ + 1)).take(100).toList())
}
