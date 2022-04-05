package com.github.imcamilo.review.typesystem

object RockingInheritance extends App {

  //convenience
  trait Writer[C] {
    def write(value: C): Unit
  }
  trait Closeable {
    def close(status: Int): Unit
  }
  trait GenericStream[C] {
    //some methods
    def foreach(f: C => Unit): Unit
  }
  def processStream[C](stream: GenericStream[C] with Writer[C] with Closeable): Unit = {
    stream.foreach(println)
    stream.close(0)
  }
  println()
  println()

  //diamond problem
  trait Animal { def name: String }
  trait Lyion extends Animal { override def name: String = "lyon" }
  trait Tiger extends Animal { override def name: String = "tiger" }
  class Mutant extends Lyion with Tiger // { override def name: String = "Alien" }

  val m = new Mutant
  println(m.name)
  //last override gets picked
  println()
  println()

  //the super problem + type linearization
  trait Cold {
    def print(): Unit = println("cold")
  }
  trait Green extends Cold {
    override def print(): Unit = {
      println("green")
      super.print()
    }
  }
  trait Blue extends Cold {
    override def print(): Unit = {
      println("blue")
      super.print()
    }
  }
  class Red {
    def print(): Unit = println("red")
  }
  class White extends Red with Green with Blue {
    override def print(): Unit = {
      println("white")
      super.print()
    }
  }
  val color = new White()
  color.print()
  /*
  white
  blue
  green
  cold

  So in scala compiler:
    Cold = AnyRef with <Cold>
    Green = Cold with <Green> => AnyRef with <Cold> with <Green>
    Blue = AnyRef with <Blue> => AnyRef with <Cold> with <Blue>
    Red = AnyRef with <Red>

    White = Red with Green with Blue with <White>
      AnyRef with <Red>
      with (AnyRef with <Cold> with <Green>)
      with (AnyRef with <Cold> with <Blue>)
      with <White>

    The follow is called type linearization:

    White =
      AnyRef with <Red> with <Cold> with <Green> with <Blue> with <White>
                                                        ^--< super <---^
                                           ^--< super <--^
                              ^--< super <--^
                   ^--< super <--^
     This will take a look at the type immediatly to the left in this type information

   */
}
