package com.github.imcamilo.review.oop

object Objects extends App {

  //SCALA DOESNOT HAVE CLASS-LEVEL FUNCTIONALITY ("STATIC")
  //DOESN'T HAVE STATIC VALUE/METHODS

  object Person { //type + its only instance
    //"static"/"class"-level functionality
    val N_EYES = 2
    def canFly: Boolean = false
    def apply(mother: Person, father: Person): Person = new Person("Bobbie")
  }
  class Person(val name: String) {
    //instance-level functionality
  }
  //THIS PATTERN OF WRITING CLASSES AND OBJECTS WITH THE SAME NAME, IN THE SAME SCOPE IS CALLED
  //COMPANIONS

  println(Person.N_EYES)
  println(Person.canFly)

  val person1 = Person
  val person2 = Person
  println(person1 == person2)

  //SCALA OBJECT == SINGLETON INSTANCE
  val mary = new Person("Mary")
  val john = new Person("John")
  println(mary == john)

  val bobbie = Person(mary, john)

  //Scala application == Scala Object with
  //def main(args: Array[String]): Unit

}
