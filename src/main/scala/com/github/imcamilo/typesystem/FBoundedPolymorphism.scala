package com.github.imcamilo.typesystem

object FBoundedPolymorphism extends App {

  /*
  trait Animal_0 {
    def breed(): List[Animal_0]
  }
  trait Cat_0 extends Animal_0 {
    override def breed(): List[Animal_0] = ??? //List Of Cat_0
  }
  trait Dog_0 extends Animal_0 {
    override def breed(): List[Animal_0] = ??? //List Of Dog_0
  }
  how do I make the compiler enforce this somehow on me?
  */

  //SOLUTION 1 - naive - because is covariant, but still allows errors
  trait Animal_1 {
    def breed(): List[Animal_1]
  }
  trait Cat_1 extends Animal_1 {
    override def breed(): List[Cat_1] = ??? //List Of Cat
  }
  trait Dog_1 extends Animal_1 {
    override def breed(): List[Cat_1] = ??? //List Of Dog
  }

  //SOLUTION 2 - F-Bounded Polymorphism
  trait Animal_2[C <: Animal_2[C]] { //recursive type: F-Bounded Polymorphism
    def breed(): List[Animal_2[C]]
  }
  trait Cat_2 extends Animal_2[Cat_2] {
    override def breed(): List[Animal_2[Cat_2]] = ??? //List Of Cat
  }
  trait Dog_2 extends Animal_2[Dog_2] {
    override def breed(): List[Animal_2[Dog_2]] = ??? //List Of Dog_2
  }

  trait Entity[E <: Entity[E]] //ORM

  class Person extends Comparable[Person] {
    override def compareTo(o: Person): Int = ???
  }

  class Crocodile_2 extends Animal_2[Dog_2] {
    override def breed(): List[Animal_2[Dog_2]] = ???
  }
  //how do I enforce, or how do I make the compiler enforce,
  //that the class are I defining it and the type C that Im annotating it with
  //are the same


  //SOLUTION 3 - F-Bounded Polymorphism + Self Types
  //recursive type: F-Bounded Polymorphism
  trait Animal_3[C <: Animal_3[C]] { self: C =>
    def breed(): List[Animal_3[C]]
  }
  trait Cat_3 extends Animal_3[Cat_3] {
    override def breed(): List[Animal_3[Cat_3]] = ??? //List Of Cat_3
  }
  trait Dog_3 extends Animal_3[Dog_3] {
    override def breed(): List[Animal_3[Dog_3]] = ??? //List Of Dog_3
  }
  //class Crocodile extends Animal_3[Dog] {
  //  override def breed(): List[Animal_3[Crocodile]] = ???
  //}

  trait Fish_3 extends Animal_3[Fish_3]
  class Shark_3 extends Fish_3 {
    override def breed(): List[Animal_3[Fish_3]] = List(new Cod_3) //cod is an animal fish but this is wrong
  }
  class Cod_3 extends Fish_3 {
    override def breed(): List[Animal_3[Fish_3]] = ???
  }
  //this is a fundamental limitation
  //once we bring our class hirarchy down one level, en FBP stops being effective

  //solution 4 - with type classes
  /*
  trait Animal_4
  trait CanBreed[A] {
    def breed(value: A): List[A]
  }
  class Dog_4 extends Animal_4
  object Dog_4 {
    implicit object Dog_4sCanBreed extends CanBreed[Dog_4] {
      override def breed(value: Dog_4): List[Dog_4] = List()
    }
  }
  implicit class CanBreedOps[A](animal: A) {
    def breed(implicit canBreed: CanBreed[A]): List[A] =
      canBreed.breed(animal)
  }
  */

  //val dog = new Dog_4
  //dog.breed //List[Dog]!!
  /*
  new CanBreedOps[Dog](dog).breed
  implicit value to pass to breed: Dog.DogsCanBreed
  */

  //solution 5 - pure type classes
  //type class
  trait Animal[A] {
    def breed(value: A): List[A]
  }
  ///type classes instances
  class Dog
  object Dog {
    implicit object DogAnimal extends Animal[Dog] {
      override def breed(value: Dog): List[Dog] = List()
    }
  }
  class Cat
  object Cat {
    implicit object DogAnimal extends Animal[Cat] {
      override def breed(value: Cat): List[Cat] = List()
    }
  }
  //conversion class
  implicit class AnimalEnrichment[A](animal: A) {
    def breed(implicit animalTypeClassInstance: Animal[A]): List[A] =
      animalTypeClassInstance.breed(animal)
  }
  val dog = new Dog
  dog.breed
  val cat = new Cat
  cat.breed






}
