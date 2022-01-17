package com.github.imcamilo.review.oop

object Inheritance extends App {

  //single class inheritance
  sealed class Animal {
    //def eat = println("ñam ñam")
    val creatureType = "wild"
    def eat: Unit = println("ñam ñam")
  }
  sealed class Cat extends Animal {
    def nomnom = {
      eat
      println("nomnom")
    }
  }
  val cat = new Cat
  //cat.eat
  cat.nomnom

  //constructors
  sealed class Person(name: String, age: Int) {
    def this(name: String) = this(name, 0)
  }
  sealed class Adult(name: String, age: Int, creditCard: Int) extends Person(name, age)
  sealed class Adult2(name: String, age: Int, creditCard: Int) extends Person(name)

  //overriding
  sealed class Dog(override val creatureType: String) extends Animal {
    //override val creatureType = "domestic"
    override def eat: Unit = {
      super.eat
      println("nom, nom, nom")
    }
  }
  //YOU CAN OVERRIDE THE VALUE IN THE IMPLEMENTATION AS THE CLASS PARAMETERS
  //val dog = new Dog
  val dog = new Dog("might be domestic")
  dog.eat
  println(dog.creatureType)

  //TYPE SUBSTITUTION (BROAD: POLYMORPHISM)
  val unknownAnimal: Animal = new Dog("T800")
  unknownAnimal.eat

  //OVERRIDING VS OVERLOADING
  //super
  //preventing override
  //1. keyword final on member
  //2. final on the entire class
  //3. seal the class, extended classes in the file, prevent extension in others files

}
