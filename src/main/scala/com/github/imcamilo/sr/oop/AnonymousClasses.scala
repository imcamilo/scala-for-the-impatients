package com.github.imcamilo.sr.oop

object AnonymousClasses extends App {

  abstract class Animal {
    def eat(): Unit
  }

  //anonymous class
  val funnyAnimal = new Animal {
    override def eat(): Unit = println("hahahahahah")
  }
  /* equivalent with
  class AnonymousClasses$$anon$1 extends Animal {
    override def eat(): Unit = println("")
  }
  val funnyAnimal = new AnonymousClasses$$anon$1
  */

  println(funnyAnimal.getClass)

  class Person(name: String) {
    def sayHi(): Unit = println(s"Hi my name is $name")
  }

  val fim = new Person("Fim") {
    override def sayHi(): Unit = println("hi bla bla bla Fim")
  }

  fim.sayHi()

}