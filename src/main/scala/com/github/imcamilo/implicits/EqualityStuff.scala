package com.github.imcamilo.implicits

import com.github.imcamilo.implicits.TypeClasses.User

object EqualityStuff extends App {

  trait EqualityTypeClass[A] {
    //def compare(first: A, second: A): Boolean
    def apply(first: A, second: A): Boolean
  }

  implicit object NamesComparator extends EqualityTypeClass[User] {
    def apply(first: User, second: User): Boolean = first.name == second.name
  }

  object FullyComparator extends EqualityTypeClass[User] {
    def apply(first: User, second: User): Boolean = first.name == second.name && first.email == second.email
  }
  //Type class pattern for equality
  object EqualityTypeClass {
    def apply[A](a: A, b: A)(implicit equalizer: EqualityTypeClass[A]): Boolean = equalizer.apply(a, b)
  }

  val eren = User("Eren", 24, "eren@aot")
  val eren2 = User("Eren", 24, "eren2@aot")
  val zeke = User("Zeke", 34, "zeke@aot")
  val grisha = User("Grisha", 44, "gri@aot")
  val anotherGrisha = User("Grisha", 44, "grisha@aot")
  //AD-HOC Polymorphism
  println(NamesComparator(eren, zeke))
  println(FullyComparator(eren, zeke))
  println(EqualityTypeClass(grisha, anotherGrisha))

  //implicit conversion
  implicit class EqualityEnrichment[C](value: C) { //TypeSafe
    def ===(anotherValue: C)(implicit equalityTypeClass: EqualityTypeClass[C]): Boolean = equalityTypeClass.apply(value, anotherValue)
    def !==(anotherValue: C)(implicit equalityTypeClass: EqualityTypeClass[C]): Boolean = !equalityTypeClass.apply(value, anotherValue)
  }

  println("check implicit conversions === : " + (eren === eren2))
  /*
  eren.===(eren2) //infix
  there is a === method in User? No.
  so it will try to Wrap User in something that has === method
  new EqualityEnrichment[User](eren).===(eren2)
  === also takes an implicit equalizer
  new EqualityEnrichment[User](eren).===(eren2)(NamesComparator)
   */
  println("check implicit conversions !==: " + (eren !== eren))
  println("check implicit conversions !==: " + (eren !== grisha))

  /*
  TYPE SAFE
  println(eren == 2)
  println(eren === 3)
   */
}
