package com.github.imcamilo.sr.functional

object TuplesAndMaps extends App {

  //val aTuple = new Tuple2(2, "Hello Scala Review")
  //val aTuple = Tuple2(2, "Hello Scala Review")
  val aTuple = (2, "Hello Scala Review")

  println(aTuple._2)
  println(aTuple.copy(_2 = "Bye bye Kotlin"))
  println(aTuple.swap)

  //val newMap = Map()  //empty map
  val newMap = Map(("Jim", 22), "Scarlet" -> 24).withDefaultValue(-1)
  //a -> b is syntactic sugar for (a, b)
  println(newMap)
  println(newMap.contains("Jim"))
  println(newMap("Jim"))
  println(newMap("Jims")) //no such element ex

  //functions
  println(newMap.map(pair => pair._1.toUpperCase() -> 2 * pair._2))
  println(newMap.view.filterKeys(key => key.startsWith("Sca")).toMap)
  println(newMap.view.mapValues(values => "0000-4213-11" + values).toMap)

  //conversions
  val xMap = Map(("Jim", 232), "JIM" -> 132).withDefaultValue(-1)
  println(xMap)
  println(xMap.map(pair => pair._1.toLowerCase() -> pair._2)) //carefull mapping keys

  println(newMap.toList)
  println(List("A" -> 1, ("B", 2)).toMap)
  val simpleList = List("Daniel", "DDL", "Camilo")
  println(simpleList.groupBy(key => key.charAt(0))) //using key charAt

  val secMap = Map(
    (1, "Jorquera"),
    (2, "Random"),
    (3, "Jorquera"),
    (4, "Random"),
    (5, "Jorquera"),
    (6, "Random"),
    (7, "Jim"))
  println(secMap.groupBy(tuple => tuple._2))

}
