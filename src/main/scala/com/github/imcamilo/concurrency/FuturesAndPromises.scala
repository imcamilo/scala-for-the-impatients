package com.github.imcamilo.concurrency

//important scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object FuturesAndPromises extends App {

  //Futures are a functional way to compute something in parallel or in another thread
  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }

  val aFuture = Future {
    calculateMeaningOfLife //this runs in ANOTHER thread
  } //(global) which is passed by the compiler

  println(aFuture.value) //Option[Try[Int]]
  println("Waiting on the future")
  aFuture.onComplete { //(t => t match {
    case Success(meaningOfLife) => println(s"Meaning of life is $meaningOfLife")
    case Failure(exception) => println(s"I have failed with $exception")
  } //some Thread ran this, which one? I dont know
  Thread.sleep(3000)

}
