package com.github.imcamilo.cats.review

object Implicits {

  // 1. Implicits classes // take a single argument all the time
  case class Person(name: String) {
    def greet: String = s"Hi, my name is $name"
  }
  implicit class ImpersonableString(name: String) {
    def greet: String = Person(name).greet // extension method
  }

  // 2. Importing implicit conversions in scope
  import scala.concurrent.duration._
  val duration = 1.second

  // 3. Implicits argument and values
  def increment(x: Int)(implicit amount: Int) = x + amount
  implicit val defaultAmount = 10
  increment(2) // (10)

  // 4. more complex
  trait JSONSerializer[T] {
    def toJson(value: T): String
  }
  def listToJSON[T](list: List[T])(implicit serializer: JSONSerializer[T]): String = {
    list.map(value => serializer.toJson(value)).mkString("[", ",", "]")
  }
  // use
  implicit val personSerializer: JSONSerializer[Person] = new JSONSerializer[Person] {
    override def toJson(value: Person): String =
      s"""
         |{ "name" : "${value.name}" }
         |""".stripMargin
  }
  val personJSON = listToJSON(List(Person("Camilo"), Person("J")))

  // An implicit argument is used to PROVE THE EXISTENCE of a type

  // 5. Implicit methods
  implicit def oneArgCaseClassSerializer[T <: Product]: JSONSerializer[T] = new JSONSerializer[T] {
    override def toJson(value: T): String =
      s"""
         |{ "${value.productElementName(0)}": "${value.productElement(0)}" }
         |""".stripMargin.trim
  }
  case class Cat(catName: String)
  val catsToJSON = listToJSON(List(Cat("A"), Cat("B")))
  // in the background... val catsToJSON = listToJSON(List(Cat("A"), Cat("B")))(oneArgCaseClassSerializer[Cat])
  // An implicit method is used to PROVE THE EXISTENCE of a type

  // 6.Implicit conversions (discouraged)

  def main(args: Array[String]): Unit = {
    println(oneArgCaseClassSerializer[Cat].toJson(Cat("Gardfield")))
    println(oneArgCaseClassSerializer[Person].toJson(Person("David")))
  }

}
