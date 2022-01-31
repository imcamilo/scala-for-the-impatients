package com.github.imcamilo.implicits

object TypeClasses extends App {

  //option 1
  trait HTMLWritable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    def toHtml: String = s"<p>$name - $age - $email</p>"
  }

  User("Eren", 22, "").toHtml

  /*
  this works but it have two bigs disadvantages:
  1. Only work for the types we writes, for other types we'd need write converters
  2. One implementation out of quite a number
   */

  //option 2 - pattern matching
  object HTMLSerializerPM {
    def serializeToHtml(value: Any) = value match {
      case User(n, a, e) =>
      case _ =>
    }
  }

  /*
  1. lost the type safety
  2. need to modify this code every time for new objects
  3. still one implementation
   */

  //option 3
  //type class
  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  //type class instance
  object UserSerializer extends HTMLSerializer[User] {
    def serialize(user: User): String = s"<p>${user.name} - ${user.age} - ${user.email}</p>"
  }

  val eren = User("Eren", 22, "eren@aot")
  println(UserSerializer.serialize(eren))

  //1. we can define serializers for other types

  import java.util.Date

  //type class instance
  object DateSerializer extends HTMLSerializer[Date] {
    override def serialize(value: Date): String = s"<p>${value.toString}</p>"
  }

  //2. we can define multiple serializers for a certain type
  //type class instance
  object PartialUserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<p>${user.name}</p>"
  }

  //TYPE CLASS
  trait MyTypeClassTemplate[A] {
    def action(value: A): String
  }

  /*
  A normal class describes a collection of methods and properties that something must have in order to belong to that specific type
  - On method parameters for example String, then it is known to support the length operation
  - The type checker for the compile can use this information at compile time to find errors in the source code - STATIC TYPE CHECKING

  A type class as opposed to a normal class lifts this concept to a higher level, applying it to types
  Describing a collection of methods and properties that a Type must have in order to belong to that specific type class
   */

  //equality
  trait EqualityTypeClass[A] {
    //def compare(first: A, second: A): Boolean
    def apply(first: A, second: A): Boolean
  }

  object NamesComparator extends EqualityTypeClass[User] {
    def apply(first: User, second: User): Boolean = first.name == second.name
  }

  object NamesAndEmailComparator extends EqualityTypeClass[User] {
    def apply(first: User, second: User): Boolean = first.name == second.name && first.email == second.email
  }

  val zeke = User("Zeke", 34, "zeke@aot")
  println(NamesComparator.apply(eren, zeke))
  println(NamesAndEmailComparator.apply(eren, zeke))

}
