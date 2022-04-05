package com.github.imcamilo.circe

import java.util.UUID

object Decoders extends App {

  // decoders can fail, they has a great error handling

  import io.circe.syntax._
  import io.circe.{Decoder, Json}

  val jsonTolkien = Json.obj("name" -> "JRR".asJson)
  val jsonInvalidTolkien = Json.obj("name" -> 8766.asJson)

  val jsonArticle = Json.obj(
    "id" -> "e636570a-cbad-48ca-b776-6a0572d1b4a0".asJson,
    "title" -> "LoTR".asJson,
    "content" -> "Lord of the Rings trilogy".asJson,
    "author" -> jsonTolkien.asJson
  )

  // if I have a case class and his fields has decoders, maybe should I use decoders.forProduct? Yeah
  implicit val authorDecoder: Decoder[Author] = Decoder.forProduct2("name", "bio")(Author.apply)

  println(authorDecoder(jsonTolkien.hcursor)) // OK
  println(jsonTolkien.as[Author]) // OK
  println(authorDecoder(jsonInvalidTolkien.hcursor)) // error handling - validation structure from cats

  // monadic decoder
  val articleDecoder: Decoder[Article] = json =>
    for {
      id <- json.get[UUID]("id")
      title <- json.get[String]("title")
      content <- json.get[String]("content")
      author <- json.get[Author]("author")
    } yield Article(id, title, content, author)

  implicit val articleDecoderFunctor: Decoder[Article] =
    Decoder.forProduct4("id", "title", "content", "author")(Article.apply)

  /** this works as intended using articleDecoder, but we lost the ability to accumulate errors, I should have multiple
   *  errors here but only get one. This behavior makes sense if we consider the differences between monad (for
   *  comprehension) and applicative functors which is Decoder.forProduct(4) uses.
   *
   *  Monads and Aplicative Functors they are both used to model effectfull computations and in that particular case the
   *  effect, is the ability to fail with a decoding fail
   *
   *  The Aplicative Functors they are used to aggregate independent or in parallel computations, which is why you can
   *  use them to accumulate failures
   *
   *  The monads in the other hand, they describe a dependent computation where each operation can access the result of
   *  the previous one.
   *
   *  This is why the monads cant accumulate errors, because you decode one field you must first decoded the previous
   *  one and if for some reason you can decode the first field, then you wont even attempt the second, bacause it
   *  wouldn't make sense, monads always returned the first encountered error
   *
   *  This is how a monadic decoder enables a field to depend on the value of a previously decoder field implicit val
   *  articleDecoder: Decoder[Article] = json => for { title <- json.get[String]("title") id =
   *  UUID.nameUUIDFromBytes(title.getBytes()) content <- json.get[String]("content") author <-
   *  json.get[Author]("author") } yield Article(id, title, content, author) articleDecoder(jsonArticle.hcursor)
   */

  // articleDecoder(jsonTolkien.hcursor)
  println(articleDecoderFunctor(jsonTolkien.hcursor))
  println(articleDecoderFunctor.decodeAccumulating(jsonTolkien.hcursor))

  val xjsonString: String = jsonArticle.spaces2

  import io.circe.parser.parse
  println(parse(xjsonString).flatMap(_.as[Article])) // invalid json in the left side, valid json in the right side

}
