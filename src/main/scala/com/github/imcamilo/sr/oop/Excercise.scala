package com.github.imcamilo.sr.oop

object Excercise extends App {
  val author = new Writer("Charles", "Dickens", 1812)
  val falseAuthor = new Writer("Charles", "Dickens", 1812)
  val novel = new Novel("Great Expectations", 1861, author)

  println(novel.autorAge)
  println(novel.isWrittenBy(author))
  println(novel.isWrittenBy(falseAuthor))

  val counter = new Counter
  counter.inc.inc.inc.inc.inc.print
  counter.inc(10).print

  counter.dec.dec.dec.dec.dec.print
  counter.dec(10).print
}

class Writer(name: String, surName: String, val year: Int) {
  def fullName: String = s"$name $surName"
}

class Novel(name: String, yearOfRelease: Int, author: Writer) {
  def autorAge: Int = yearOfRelease - author.year
  def isWrittenBy(author: Writer): Boolean = author == this.author
  def copy(newYearOfRelease: Int): Novel = new Novel(name, newYearOfRelease, author)
}

/*
class Counter(value: Int) {
  def count: Int = value
}
 */
class Counter(val count: Int = 0) {
  def inc: Counter = {
    new Counter(count + 1) //inmutability
  }

  def dec: Counter = {
    new Counter(count - 1) //inmutability
  }

  def inc(n: Int): Counter = {
    if (n <= 0) this
    else inc.inc(n - 1)
  }

  def dec(n: Int): Counter = {
    if (n <= 0) this
    else dec.dec(n - 1)
  }

  def print: Unit = println(count)
}
