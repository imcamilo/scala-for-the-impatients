package com.github.imcamilo.circe

object SemiAutomaticDerivation extends App {

  // using macros to provide encoders and decoders

  import io.circe.generic.semiauto._
  import io.circe.syntax._
  import io.circe.{Codec, Decoder, Encoder}

  implicit val authorDecoder: Decoder[Author] = deriveDecoder[Author]
  implicit val authorEncoder: Encoder[Author] = deriveEncoder[Author]

  // for many types you going to want bidirectional serialization, the encoder and decoder,
  // this can be done in a single step by requesting a codec
  implicit val authorCodec: Codec[Author] = deriveCodec[Author] // combination of encoder/decoder in a single structure

  // context bounds
  def encodingBoilerplate[C](author: Author)(implicit authorEncoder: Encoder[Author]): String = {
    author.asJson.spaces2
  }

  // it just going to be identical,
  // except that i cant use serializer by name because the compiler injects a implicit for us
  def encodingSugar[Author: Encoder](author: Author): String = {
    val encoder = implicitly[Encoder[Author]]
    encoder(author).spaces2
  }

  val simpleAuthor = Author("JRR", Some("something"))
  println(simpleAuthor.asJson.spaces2)
  println(encodingBoilerplate(simpleAuthor))
  println(encodingSugar(simpleAuthor))

}
