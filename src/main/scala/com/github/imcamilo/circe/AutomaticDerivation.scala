package com.github.imcamilo.circe

object AutomaticDerivation extends App {

  import io.circe.syntax._
  import io.circe.{Decoder, Encoder}
  // this is considered harmfully, prefer semi-automatic derivation
  // encoders and decoders, works well but maybe with boilerplate

  import io.circe.generic.auto._ // deriving for us encoder and decoders any case class

  implicitly[Encoder[Author]]
  implicitly[Encoder[Article]]
  implicitly[Decoder[Author]]
  implicitly[Decoder[Article]]

  println(Author("JRR", Some("something")).asJson.spaces2)

}
