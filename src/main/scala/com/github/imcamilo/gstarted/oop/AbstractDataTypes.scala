package com.github.imcamilo.gstarted.oop

object AbstractDataTypes extends App {

  //abstract
  //abstract classes cannot be instantiate
  //abstract classes can have abstract and non abstract members
  abstract class Animal {
    val creatureType: String = "wild"
    def eat: Unit
  }
  class Dog extends Animal {
    override val creatureType: String = "canine"
    override def eat: Unit = println("crunch crunch")
  }

  //trait
  //traits can have abstract and non abstract members
  //describe what classes do
  trait Carnivore {
    def eat(animal: Animal): Unit
    val preferredMeal: String = "fresh meat"
  }
  trait ColdBlooded
  class Crocodrile extends Animal with Carnivore {
    override val creatureType: String = "killer croc"
    override def eat: Unit = "gr gr gr"
    override def eat(animal: Animal): Unit = println(s"I'm a croc and I'm eating ${animal.creatureType}")
  }
  val dog = new Dog
  val croc = new Crocodrile
  croc.eat(dog)

  //TRAITS VS ABSTRACT CLASSES
  //1. traits cannot have constructor parameters
  //2. you can only extend one class, but you can mix multiple traits
  //3. Traits = Behavior, Abstract Class = "Thing"

}
