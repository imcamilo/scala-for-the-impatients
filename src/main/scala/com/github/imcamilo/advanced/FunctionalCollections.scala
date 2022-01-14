package com.github.imcamilo.advanced

import scala.annotation.tailrec

trait IMSet[A] extends (A => Boolean) {
  def apply(a: A): Boolean = contains(a) //like a original Set
  def contains(a: A): Boolean

  def +(a: A): IMSet[A]

  def ++(anotherSet: IMSet[A]): IMSet[A]

  def map[B](f: A => B): IMSet[B]

  def flatMap[B](f: A => IMSet[B]): IMSet[B]

  def filter(predicate: A => Boolean): IMSet[A]

  def foreach(f: A => Unit): Unit
}

class EmptySet[A] extends IMSet[A] {
  def contains(a: A): Boolean = false

  def +(elem: A): IMSet[A] = new NonEmptySet[A](elem, this)

  def ++(anotherSet: IMSet[A]): IMSet[A] = anotherSet

  def map[B](f: A => B): IMSet[B] = new EmptySet[B]

  def flatMap[B](f: A => IMSet[B]): IMSet[B] = new EmptySet[B]

  def filter(predicate: A => Boolean): IMSet[A] = this

  def foreach(f: A => Unit): Unit = ()
}

class NonEmptySet[A](head: A, tail: IMSet[A]) extends IMSet[A] {
  def contains(elem: A): Boolean = elem == head || tail.contains(elem)

  def +(elem: A): IMSet[A] = if (this contains elem) this else new NonEmptySet[A](elem, this)

  /**
   * how it works, by recursion and polimorfism
   * ex.
   * [1,2,3] ++ [4,5] this would be
   * tail of the first
   * [2, 3] ++ [4, 5] + 1 =
   * [3] ++ [4, 5] + 1 + 2 =
   * [] ++ [4, 5] + 1 + 2 + 3 =
   * [4, 5] + 1 + 2 + 3 = [4, 5, 1, 2, 3]
   *
   * @param anotherSet
   * @return
   */
  def ++(anotherSet: IMSet[A]): IMSet[A] = tail ++ anotherSet + head

  def map[B](f: A => B): IMSet[B] = tail.map(f) + f(head)

  def flatMap[B](f: A => IMSet[B]): IMSet[B] = tail.flatMap(f) ++ f(head)

  def filter(predicate: A => Boolean): IMSet[A] = {
    val filteredTail = tail.filter(predicate)
    if (predicate(head)) filteredTail + head
    else filteredTail
  }

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }

}

object IMSet {
  /**
   * val imset = IMSet(1, 2, 3) = buildSet(seq(1,2,3), [])
   * = buildSet(seq(2,3), [] + 1)
   * = buildSet(seq(3), [1] + 2)
   * = buildSet(seq(), [1, 2] +3)
   * = [1,2,3]
   */
  def apply[A](values: A*): IMSet[A] = {
    @tailrec
    def buildSet(valSeq: Seq[A], acc: IMSet[A]): IMSet[A] = {
      if (valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)
    }

    buildSet(values.toSeq, new EmptySet[A])
  }
}

//exercise implement a functional set
object IMSetPlayground extends App {
  val s = IMSet(1, 2, 3, 4)
  s + 5 ++ IMSet(0, -1, -2) + 3 map (_ * 10) foreach println
  println("-------")
  s flatMap(a => IMSet(a, a * 10)) foreach println
  println("-------")
  s filter(_ % 3 == 0) foreach println
}