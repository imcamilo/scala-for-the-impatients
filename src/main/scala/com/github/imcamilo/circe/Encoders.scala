package com.github.imcamilo.circe

import java.util.UUID

object Encoders extends App {

  import io.circe.{Encoder, Json}
  import io.circe.syntax._

  val tolkien = Author("J. R. R. Tolkien", Some("hhhahaha"))
  val article = Article(UUID.randomUUID(), "LoTR", "Lord of the Rings trilogy", tolkien)

  // encoders for some type T describes how values of type T are to be turned into json
  // encoders cant fail and the can be defined simply as a function from T to JSON
  implicit val authorEncoder: Encoder[Author] = author =>
    Json.obj("name" -> author.name.asJson, "bio" -> author.bio.asJson)

  /*
    When you have an Encoder[A], you get:
      - Encoder[Option[A]]
      - Encoder[List[A]]
      - Encoder[Array[A]]
    and more for free
   */

  implicit val articleEncoder: Encoder[Article] = article =>
    Json.obj(
      "id" -> article.id.asJson,
      "title" -> article.title.asJson,
      "content" -> article.content.asJson,
      "author" -> article.author.asJson // I can write the article author as JSON using implicitly the author encoder
    )

  println(article.asJson.spaces2)
  val articles = List.fill(5)(article)
  println(articles.asJson.spaces2)

}
