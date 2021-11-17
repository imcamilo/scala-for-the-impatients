package com.github.imcamilo.sr.functional

import scala.util.{Failure, Random, Success, Try}

object HandlingFailure extends App {

  //create success and a failure
  val aSuccess = Success(3)
  val aFailure = Failure(new RuntimeException("fail"))

  println(aSuccess)
  println(aFailure)

  def unsafeMethod(): String = throw new RuntimeException("the joke is on you")

  //Try objects via the apply method
  val potentialFailure = Try(unsafeMethod())
  println(potentialFailure)

  //sugar
  val anotherPotentialFailure = Try {
    //code that migth throw
  }

  //utilities
  println(potentialFailure.isSuccess)
  println(potentialFailure.isFailure)

  //orElse
  def backupMethod(): String = "A valid result"

  val fallbackTry = Try(unsafeMethod()).orElse(Try(backupMethod()))
  println(fallbackTry)

  //if you are design the API
  def betterUnsafeMethod(): Try[String] = Failure(new RuntimeException)

  def betterBackupMethod(): Try[String] = Success("yes")

  def betterFallBack = betterUnsafeMethod().orElse(betterBackupMethod())

  def betterFallBack2 = betterUnsafeMethod() orElse betterBackupMethod()

  //map, flatMap, filter
  println(aSuccess.map(_ * 6))
  println(aSuccess.flatMap(a => Success(a * 8)))
  println(aSuccess.filter(_ > 10)) //return a failure if is false
  //for-comprenhensions

  val hostname = "localhost"
  val port = "8080"

  def renderHTML(page: String): Unit = println(page)

  class Connection {
    def get(url: String): String = {
      val random = new Random(System.nanoTime())
      if (random.nextBoolean()) "<html>...</html>"
      else throw new RuntimeException("Connection interrupted")
    }

    //
    def getSafe(url: String): Try[String] = Try(get(url))

  }

  object HttpServer {
    val random = new Random(System.nanoTime())

    def getConnection(host: String, port: String): Connection = {
      if (random.nextBoolean()) new Connection
      else throw new RuntimeException("Third party error")
    }

    //
    def getSafeConnection(host: String, port: String): Try[Connection] = Try(getConnection(host, port))
  }

  //
  val possibleConnection = HttpServer.getSafeConnection(hostname, port)
  val possibleHTML = possibleConnection.flatMap(con => con.getSafe("/index"))
  possibleHTML.foreach(renderHTML)

  //shorthand version
  HttpServer.getSafeConnection(hostname, port)
    .flatMap(conn => conn.getSafe("/home"))
    .foreach(renderHTML)

  //for-comprenhension version
  for {
    conn <- HttpServer.getSafeConnection(hostname, port)
    html <- conn.getSafe("/footer")
  } yield renderHTML(html)

}
