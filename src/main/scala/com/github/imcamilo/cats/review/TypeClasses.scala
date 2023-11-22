package com.github.imcamilo.cats.review

object TypeClasses {

  case class Person(name: String, age: Int)

  // programming pattern, offer capabilities on a certain type.

  // 1. TYPE CLASS DEFINITION (GENERIC TRAIT, ABSTRACT CLASS)
  trait JSONSerializer[T] {
    def toJSON(t: T): String
  }

  // 2. CREATE IMPLICIT TYPE CLASS INSTANCES
  implicit object StringSerializer extends JSONSerializer[String] {
    override def toJSON(t: String): String = s"\"" + t + "\""
  }
  implicit object IntSerializer extends JSONSerializer[Int] {
    override def toJSON(t: Int): String = t.toString
  }
  implicit object PersonSerializer extends JSONSerializer[Person] {
    override def toJSON(t: Person): String =
      s"""
         |{ "name" : "${t.name}", "age" : ${t.age}" }
         |""".stripMargin.trim
  }

  // 3. SOME SORT OF API - OFFER SOME API
  def convertToJSON[T](list: List[T])(implicit serializer: JSONSerializer[T]) =
    list.map(a => serializer.toJSON(a)).mkString("[", ",", "]")

  // 4. EXTENDING EXISTING TYPES , via extension methods
  object JSONSyntax {
    implicit class JSONSerializable[T](value: T)(implicit serializer: JSONSerializer[T]) {
      def toJSON: String = serializer.toJSON(value)
    }
  }
  val bob: Person = Person("Bob", 29)
  import JSONSyntax._

  def main(args: Array[String]): Unit = {
    val listPersons = List(Person("Alice", 21), Person("Javier", 45))
    println(convertToJSON(listPersons))
    println(bob.toJSON)
  }

}

object Test {

  // type class definition
  trait Summable[T] {
    def sum(elements: List[T]): T
  }

  // type class instance
  implicit object StrSummable extends Summable[String] {
    override def sum(elements: List[String]): String = elements.mkString("[", ",", "]")
  }
  implicit object IntSummable extends Summable[Int] {
    override def sum(elements: List[Int]): Int = elements.sum
  }

  def sumElements[T](list: List[T])(implicit summable: Summable[T]): T = {
    summable.sum(list)
  }
//

  def main(args: Array[String]): Unit = {
    // adhoc polymorphism
    println(sumElements(List("a", "b"))) // == "a,b"
    println(sumElements(List(1, 2))) // == 3
  }

}
