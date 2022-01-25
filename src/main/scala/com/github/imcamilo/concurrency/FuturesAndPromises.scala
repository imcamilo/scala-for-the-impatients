package com.github.imcamilo.concurrency

//important scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Random, Success}

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

  //mini social network
  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile): Unit = {
      println(s"${this.name} poking ${anotherProfile.name}")
    }
  }

  object SocialNetwork {
    //FAKE DB AS MAP
    val names = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb.id.2-bill" -> "Bill",
      "fb.id.2-dummy" -> "Dummy",
    )
    val friends = Map(
      "fb.id.1-zuck" -> "fb.id.2-bill"
    )
    val random = new Random()

    //API
    def fetchProfile(id: String): Future[Profile] = Future {
      //fetching from db
      Thread.sleep(random.nextInt(300))
      Profile(id, names(id))
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      Thread.sleep(random.nextInt(400))
      val bfId = friends(profile.id)
      Profile(bfId, names(bfId))
    }

  }

  //CLIENT - mark to poke bill
  val mark = SocialNetwork.fetchProfile("fb.id.1-zuck")
  /*
  mark.onComplete {
    case Success(markProfile) =>
      val bill = SocialNetwork.fetchBestFriend(markProfile)
      bill.onComplete {
        case Success(billProfile) => markProfile.poke(billProfile)
        case Failure(e) => e.printStackTrace()
      }
    case Failure(e) => e.printStackTrace()
  }
  Thread.sleep(1000)
  */


  //functional composition of futures
  //big tree, map, flatMap and filter

  //it would be a Future[String] or if fails an Exception
  val nameOnTheWall = mark.map(profile => profile.name)
  //From future to another Future
  val markBestFriend = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))
  //This would be a future of the same type or if the predicate fails -> no such element exception
  val zuckBestFriendRestricted = markBestFriend.filter(profile => profile.name.startsWith("Z"))

  //having the big tree off course I could write
  //for-comprehension
  for {
    mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } yield mark.poke(bill)
  Thread.sleep(1000)

  //fallbacks
  //in case the future fails with an exception, then we can recover that by returning a well defined profile
  val aProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recover {
    case e: Throwable => Profile("fb.id.0-dummy", "Default Profile")
  }
  //case when we dont want return a profile itself, but to fetch another from social network
  //in the case of the second future also return an exception, tough luck
  val aFetchProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recoverWith {
    case e: Throwable => SocialNetwork.fetchProfile("fb.id.0-dummy")
  }

  //same result by falling back and we can achieve the same result
  //in case of exception in the first future, will take the second future, but if it fails too
  //finally the exception of the first future will be contained in the failure
  val fallBackResult = SocialNetwork.fetchProfile("unknown id").fallbackTo(SocialNetwork.fetchProfile("fb.id.0-dummy"))

}
