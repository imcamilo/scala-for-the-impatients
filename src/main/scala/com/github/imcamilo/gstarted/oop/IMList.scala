package com.github.imcamilo.gstarted.oop

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

object IMListTest extends App {
  val imList = new Cons(1, Empty)
  println(imList.head)
  val imList2 = new Cons(1, new Cons(2, new Cons(3, Empty)))
  println(imList2.tail.head)
  println(imList2.add(4).head)
  println(imList2.isEmpty)
  //polymorphic call
  println(imList2.toString)
}