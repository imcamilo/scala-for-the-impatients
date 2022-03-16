package com.github.imcamilo.review.functional

import java.util.Random

object Options extends App {

  val firstOption: Option[Int] = Some(27)
  val noOption: Option[Int] = None

  println(firstOption)
  println(noOption)

  //work with unsafe APIs
  def unsafeMethod(): String = null

  def restul = Some(null) //WRONG
  //SOME SHOULD ALWAYS HAVE A VALID VALUE INSIDE
  val result = Option(unsafeMethod()) //build a Some or None
  println(result)

  //chained methods
  def backupMethod(): String = "Valid result"

  val chainedResult: Option[String] = Option(unsafeMethod()).orElse(Option(backupMethod()))
  val chainedResult2: Serializable = Option(unsafeMethod()).getOrElse(Option(backupMethod()))
  println("chained result 2 " + chainedResult2)
  //Design unsafe APIs
  def betterUnsafeMethod(): Option[String] = None

  def betterBackupMethod(): Option[String] = Some("Valid result")

  //def betterChainedResult = betterUnsafeMethod().orElse(betterBackupMethod())
  def betterChainedResult = betterUnsafeMethod() orElse betterBackupMethod()

  //functions on Options
  println(firstOption.isEmpty)
  println(firstOption.get) //UNSAFE - DO NOT USE THIS

  //<3
  //map, flatMap, filter
  println(firstOption.map(_ * 2))
  println(firstOption.filter(a => a > 10)) //Some(27)
  println(firstOption.filter(a => a > 100)) //None
  println(firstOption.flatMap(a => Option(a * 10))) //Some(270)

  //Functions
  val config: Map[String, String] = Map(
    //fetch from elsewhere
    "host" -> "192.2.13.1",
    "port" -> "80"
  )

  class Connection {
    def connect = "Connected" //connect to some server
  }

  object Connection {
    val random = new Random(System.nanoTime())

    def apply(host: String, port: String): Option[Connection] =
      if (random.nextBoolean()) Some(new Connection)
      else None
  }

  //try to establish a connection
  //If so - print the connect method

  val host = config.get("host")
  val port = config.get("port")

  val connection = host.flatMap(h => port.flatMap(p => Connection(h, p)))
  val connectionStatus = connection.map(c => c.connect)
  println(connectionStatus)
  connectionStatus.foreach(println)

  //reducing this
  config.get("host")
    .flatMap(h => config.get("port")
      .flatMap(p => Connection(h, p))
      .map(conn => conn.connect))
    .foreach(println)

  //finally For-Comprenhension
  val connectionReduced = for {
    h <- config.get("host")
    p <- config.get("port")
    conn <- Connection(h, p)
  } yield conn.connect
  connectionReduced.foreach(println)

}
