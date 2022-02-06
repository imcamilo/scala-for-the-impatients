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
  implicit object UserSerializer extends HTMLSerializer[User] {
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

  object MyTypeClassTemplate {
    def apply[A](implicit instance: MyTypeClassTemplate[A]): MyTypeClassTemplate[A] = instance
  }

  /*
  A normal class describes a collection of methods and properties that something must have in order to belong to that specific type
  - On method parameters for example String, then it is known to support the length operation
  - The type checker for the compile can use this information at compile time to find errors in the source code - STATIC TYPE CHECKING

  A type class as opposed to a normal class lifts this concept to a higher level, applying it to types
  Describing a collection of methods and properties that a Type must have in order to belong to that specific type class
   */


  //pt2
  object HTMLSerializer {
    def serialize[A](value: A)(implicit serializer: HTMLSerializer[A]): String = {
      serializer.serialize(value)
    }

    def apply[A](implicit serializer: HTMLSerializer[A]): HTMLSerializer[A] = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    def serialize(value: Int): String = s"<p>bla bla bal $value</p>"
  }

  println(HTMLSerializer.serialize(8766))
  println(HTMLSerializer.serialize(eren))
  //access to the entire type class interface
  println(HTMLSerializer[User].serialize(eren))

  //enrichments

  implicit class HTMLEnrichment[C](value: C) {
    def toHTML(implicit serializer: HTMLSerializer[C]) = serializer.serialize(value)
  }

  println(eren.toHTML) //println new HTMLEnrichment[User](john).toHTML(UserSerializer)
  //COOL!
  /*
  - we can extends functionality of any type
  - differents implementations for the same type
    / choosing the implementation
    / importing the correct implicit to local scope
    / passing explicitily as implicit parameter
  - super expressive!

   */
  println(2.toHTML)
  println(eren.toHTML(PartialUserSerializer))

  /*
  - type class itself --- HTMLSerializer[C] { ... }
  - type class instances (some of which are implicit) --- UserSerializer, IntSerializer
  - conversion with implicit classes --- HTMLEnrichment
   */

  // contex bounds
  def htmlBoilerplate[C](content: C)(implicit htmlSerializer: HTMLSerializer[C]) =
    s"<p><ul><li>${content.toHTML(htmlSerializer)}</li></ul><p>"

  //it just going to be identical, except that i cant use serializer by name because the compiler injects a implicit for us
  def htmlSugar[C : HTMLSerializer](content: C) = {
    val serializer = implicitly[HTMLSerializer[C]]
    //use the serializer
    s"<p><ul><li>${content.toHTML(serializer)}</li></ul><p>"
  }

  //implicitly
  case class Permissions(mask: String)
  implicit val defaultPermissions: Permissions = Permissions("0744")

  //other part of the code we want to surface what is the implicit values for Permissions
  val standardPermissions = implicitly[Permissions]

}
