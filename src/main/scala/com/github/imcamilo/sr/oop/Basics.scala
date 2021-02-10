package com.github.imcamilo.sr.oop

object Basics extends App {

  val person = new Person("Rick", 42)
  println(person.age) //dont work without val
  println(person.x)
  person.greet("Mocha")
  person.greet()

}

//name and age are class parameters, not a class member that you can access with a dot operator.
//class parameters are not fields
//the way that you would convert a class parameter to a field, would be to add the vowel or var/val keyworkd
//constructor
class Person(name: String, val age: Int = 3) { //the result of this expression is ignored, because is a class definition
  val x = 543

  def greet(name: String): Unit = println(s"${this.name} says: hi $name") //this. refer to the class parameter

  //overloading
  def greet(): Unit = println(s"Hi $name")

  //overloading constructors
  def this(name: String) = this(name, 3)
  def this() = this("johnny b")
}
