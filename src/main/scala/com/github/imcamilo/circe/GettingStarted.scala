package com.github.imcamilo.circe

object GettingStarted extends App {

  import io.circe.Json
  import io.circe.syntax._

  val jsonString: Json = Json.fromString("valid str!")
  val jsonNumber: Json = Json.fromInt(8766)
  val jsonArray: Json = Json.arr(jsonString, jsonNumber)
  println(jsonString)
  println(jsonNumber)
  println(jsonArray)

  val anotherJsonString = "another valid one".asJson
  val anotherJsonInt = 8766.asJson
  val jsonObj = Json.obj("key" -> "value".asJson)
  println(anotherJsonString)
  println(anotherJsonInt)
  println(jsonObj)
  println(jsonString.mapString(_.toUpperCase()))
  println(jsonArray.mapArray(_.map(_.mapString(_.toUpperCase())))) // VALID STR!

  // a cursor that allows you to focus elems on structure, and modify them, and also encode the posibility of failure
  val complexObject = Json.obj("nested" -> jsonObj)
  println(complexObject.hcursor.downField("nested").top.map(_.spaces2))
  println(
    complexObject.hcursor
      .downField("nested")
      .downField("key")
      .withFocus(_.mapString(_.reverse)) // passing a fuction from jsonvalue -> jsonvalue
      .top // to the root
      .map(_.spaces2) // or 4 spaces or minified with noSpaces
  )

}
