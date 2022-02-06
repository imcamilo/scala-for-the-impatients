package com.github.imcamilo.implicits

import java.util.Date

object JsonSerialization extends App {

  //Serialize to JSON Users, Posts, Feeds

  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createdAt: Date)
  case class Feeds(user: User, posts: List[Post])

  /**
   * 1 Intermedia data types: Int, String, List, Date
   * 2 Type classes for conversions to intermediate data types
   * 3 Serialize those intermediate data types to JSON
   */

  //1.1 intermediate data type
  sealed trait JSONValue {
    def stringify: String
  }

  final case class JSONString(value: String) extends JSONValue {
    override def stringify: String = "\"" + value + "\""
  }

  final case class JSONNumber(value: Int) extends JSONValue {
    override def stringify: String = value.toString
  }

  final case class JSONArray(values: List[JSONValue]) extends JSONValue {
    override def stringify: String = values.map(_.stringify).mkString("[", "," ,"]")
  }

  /*
   {
     name: Eren,
     age: 24,
     friends: [...],
     latestPost: {
       content: Snk today,
       date: ...
     }
   }
    */
  final case class JSONObject(values: Map[String, JSONValue]) extends JSONValue {
    override def stringify: String = values.map {
      case (key, value) => "\"" + key + "\":" + value.stringify
    }.mkString("{", ",", "}")
  }

  val data: JSONObject = JSONObject(Map(
    "user" -> JSONString("Camilo"),
    "posts" -> JSONArray(
      List(
        JSONString("Snk Today,"),
        JSONNumber(8766)))
    )
  )

  println(data.stringify)

  /**
   * 2. type classes
   * 2.1 type class
   * 2.2 type class instances
   * 2.3 method to use it, pimp library to use type class
   */

  //2.1
  trait JSONConverter[C] {
    def convert(value: C): JSONValue
  }

  //2.3 conversions
  implicit class IMJSON[C](value: C) {
    def toJSON(implicit converter: JSONConverter[C]): JSONValue = {
      converter.convert(value)
    }
  }

  //2.2 existing data types
  implicit object StringConverter extends JSONConverter[String] {
    override def convert(value: String): JSONValue = JSONString(value)
  }

  implicit object NumberConverter extends JSONConverter[Int] {
    override def convert(value: Int): JSONValue = JSONNumber(value)
  }

  //custom data types
  implicit object UserConverter extends JSONConverter[User] {
    override def convert(value: User): JSONValue = JSONObject(Map(
      "name" -> JSONString(value.name),
      "age" -> JSONNumber(value.age),
      "email" -> JSONString(value.email)
    ))
  }

  implicit object PostConverter extends JSONConverter[Post] {
    override def convert(value: Post): JSONValue = JSONObject(Map(
      "content" -> JSONString(value.content),
      "createdAt" -> JSONString(value.createdAt.toString)
    ))
  }

  implicit object FeedConverter extends JSONConverter[Feeds] {
    override def convert(value: Feeds): JSONValue = JSONObject(Map(
      "user" -> value.user.toJSON,
      "posts" -> JSONArray(value.posts.map(_.toJSON))
    ))
  }

  //call stringify on result
  val now = new Date(System.currentTimeMillis())
  val eren = User("eren", 24, "eren@aot.com")
  val feed = Feeds(eren, List(
    Post("Uhh", now),
    Post("AHhh", now),
    Post("Gen", now),
  ))

  println(feed.toJSON.stringify)

}
