package com.github.imcamilo.sr.oop

class Generics {

  /*
  class IMList[+A] {
    //def add(element: A): IMList[A] = ???
    def add[B >: A](element: B): IMList[B] = ???
  }
  object IMList {
    def empty[A]: IMList[A] = ???
  }

  class IMMap[A, B]
  trait IMTrait[A]

  val listOfIntegers = new IMList[Int]
  val listOfStrings = new IMList[String]
  val emptyListOfInts = IMList.empty[Int]
   */

  //WHEN USE GENERCIS, THERE IS SOME PROBLEMS TO ANSWER..
  //VARIANCE PROBLEM
  class Animal
  class Dog extends Animal
  class Cat extends Animal

  //SI CAT EXTIENDE DE ANIMAL, UNA LISTA DE CAT EXTIENDE DE ANIMAL?
  //List[Cat] extends List[Animal]
  //WHAT IF I ADD A NEW DOG TO IT
  //catList.add(new Dog) ??? HARD QUESTION => we return a list of Animals

  //1. YES -> COVARIANCE
  class CovariantList[+A]
  val animal: Animal = new Cat
  val animalList: CovariantList[Animal] = new CovariantList[Cat]

  //2. NO -> INVARIANCE
  class InvariantList[A]
  val invariantAnimalList: InvariantList[Animal] = new InvariantList[Animal]

  //3. HELL NO -> CONTRA VARIANCE
  class ContravariantList[-A]
  val contravariantList: ContravariantList[Cat] = new ContravariantList[Animal]

  class Trainer[-A]
  val trainer: Trainer[Cat] = new Trainer[Animal]

  //BOUNDED TYPES
  //class Cage only accept type parameters which are subtypes of animal ( < )
  //class Cage only accept type parameters which are supertypes of animal ( > )
  class Cage[A <: Animal](animal: A)
  val cage = new Cage[Cat](new Cat)

  //class Car
  //val newCage = new Cage[Car](new Car)
  //generic type needs proper bounded type

}