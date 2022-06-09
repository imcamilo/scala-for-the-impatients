package com.github.imcamilo.review.typesystem

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object HigherKindedTypes extends App {

  //are a deeper generic types with some unknown type parameter at the deepest level
  trait HigherKindedType[F[_]]

  //what are HKT used for
  trait MyList[T] {
    def flatMap[B](f: T => B): MyList[B]
  }
  trait MyOption[T] {
    def flatMap[B](f: T => B): MyOption[B]
  }
  trait MyFuture[T] {
    def flatMap[B](f: T => B): MyFuture[B]
  }
  def multiply[A, B](listA: List[A], listB: List[B]): List[(A, B)] =
    for {
      a <- listA
      b <- listB
    } yield (a, b)
  def multiply[A, B](optionA: Option[A], optionB: Option[B]): Option[(A, B)] =
    for {
      a <- optionA
      b <- optionB
    } yield (a, b)
  def multiply[A, B](futureA: Future[A], futureB: Future[B]): Future[(A, B)] =
    for {
      a <- futureA
      b <- futureB
    } yield (a, b)

  //use a High Kinded Type

  //So F[_] is either a list or future or option or whatever you have
  //and the type parameter that this monad contains is A
  trait Monad[F[_], A] { //higher-kinded type class
    def flatMap[B](f: A => F[B]): F[B]
    def map[B](f: A => B): F[B]
  }
  implicit class MonadList[A](list: List[A]) extends Monad[List, A] {
    override def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)
    override def map[B](f: A => B): List[B] = list.map(f)
  }
  implicit class MonadOption[A](option: Option[A]) extends Monad[Option, A] {
    override def flatMap[B](f: A => Option[B]): Option[B] = option.flatMap(f)
    override def map[B](f: A => B): Option[B] = option.map(f)
  }

  def multiply[F[_], A, B](implicit ma: Monad[F, A], mb: Monad[F, B]): F[(A, B)] =
    for {
      a <- ma
      b <- mb
    } yield (a, b)

  val monadList = new MonadList(List(1,2,3))
  monadList.flatMap(x => List(x, x+1)) //List[Int] //Monad[List, Int] => List[Int]
  monadList.map(_ * 2) //List[Int] //Monad[List, Int] => List[Int]
  println(multiply(new MonadList(List(1, 2)), new MonadList(List("A", "B"))))
  println(multiply(new MonadOption(Some(2)), new MonadOption(Some("Scala"))))

  /*
  We wanted a single implementation for multiply, that could apply for anything that has a map
  and a flatmap, only that we used a wrapper insted of the actual types...

  Wouldn't be nice if we can auto convert our little types into the monad wrapper counterparts?
   */
  println(multiply(List(1, 2), List("A", "B")))
  println(multiply(Some(2), Some("Scala")))


}
