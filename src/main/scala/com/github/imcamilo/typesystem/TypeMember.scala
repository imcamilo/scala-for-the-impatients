package com.github.imcamilo.typesystem

object TypeMember extends App {

  class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollection {
    //abstract type members
    type AnimalType
    type BoundedAnimal <: Animal
    type SuperBoundedAnimal >: Dog <: Animal
    type AnimalC = Cat
  }

  val ac = new AnimalCollection
  //val dog: ac.AnimalType = ??? //OK
  //val cat: ac.BoundedAnimal = new Cat
  val pup: ac.SuperBoundedAnimal = new Dog
  val cat: ac.AnimalC = new Cat

  type CatAlias = Cat
  val anotherCat: CatAlias = new Cat

  //alternative to generics
  trait MyList {
    type C
    def add(element: C): MyList
  }
  class NonEmptyList(value: Int) extends MyList {
    override type C = Int
    override def add(element: Int): MyList = ???
  }

  //.type //type alias //can only do associations, doesnt support new instances
  type CatsType = cat.type
  val newCat: CatsType = cat
  //new CatsType

  /*
     enforce a type to be applicable to SOME TYPES only
   */

  trait MList {
    type A
    def head: A
    def tail: MList
  }

  trait ApplicableToNumbers {
    type A <: Number
  }

  /*
  enforce compiler check in compiler time

  class CustomList(hd: String, tl: CustomList) extends MList with ApplicableToNumbers {
    type A = String
    def head: String = hd
    def tail: MList = tl
  }
   */

  //Numbers
  class IntList(hd: Int, tl: IntList) extends MList {
    type A = Int
    def head: Int = hd
    def tail: MList = tl
  }

  //type members and type member constraints (bounds)
}
