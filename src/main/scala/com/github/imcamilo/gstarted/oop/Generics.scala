package com.github.imcamilo.gstarted.oop

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

object ListTest extends App{

  abstract class IMList[+A] {
    def head: A
    def tail: IMList[A]
    def isEmpty: Boolean
    def add[B >: A](element: B): IMList[B]
    def printElements: String
    override def toString: String = s"[$printElements]"
  }

  object Empty extends IMList[Nothing] {
    override def head: Nothing = throw new NoSuchElementException
    override def tail: IMList[Nothing] = throw new NoSuchElementException
    override def isEmpty: Boolean = true
    override def add[B >: Nothing](element: B): IMList[B] = new Cons(element, Empty)
    override def printElements: String = ""
  }

  class Cons[+A](hd: A, tl: IMList[A]) extends IMList[A] {
    override def head: A = hd
    override def tail: IMList[A] = tl
    override def isEmpty: Boolean = false
    override def add[B >: A](element: B): IMList[B] = new Cons(element, tl)
    override def printElements: String =
      if (tl.isEmpty) "" + hd
      else hd + " " + tl.printElements
  }

  val listOfIntegers: IMList[Int] = new Cons(1, new Cons(2 , new Cons(3, new Cons(4 , Empty))))
  val listOfStrings: IMList[String] = new Cons("Hello", new Cons("Scala", new Cons("World", Empty)))

  println(listOfIntegers.toString)
  println(listOfStrings.toString)

}
