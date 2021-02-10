package com.github.imcamilo.sr.oop

object CaseClasses extends App {

  //equals, hashCode, toString

  case class Person(name: String, age: Int)

  //1. Class parameters are promoted to fields
  val cml = new Person("Camilo", 26)
  println(cml.name)

  //2. Sensible toString
  //println(instance) == println(instance.toString) //syntactic sugar
  println(cml)

  //3. Equals and HashCode implemented OOB
  val cml2 = new Person("Camilo", 26)
  println(cml == cml2) //only with case classes with equals method implemented

  //4. Case Classes have handy copy method
  val cml3 = cml.copy(age = 20)
  println(cml3)

  //5. Case Classes have companion objects
  val thePerson = Person
  val mary = Person("Mary", 23)

  //6. Case Classes are serializable
  //Akka

  //7. Case Classes have extractors patterns = CCs can be used in Pattern Matching

  //object has the same features.
  case object RepOfChile {
    def name: String = "Republic Of Chile"
  }

}
