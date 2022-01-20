package com.github.imcamilo.advanced

/**
 * <h2>Monads</h2>
 * <p>Monads are a kind of types which have some fundamental operations:</p>
 * <ul>
 * <li>unit (also call pure or apply)   => construct a monad.</li>
 * <li>flatMap (also call bind)         => transforms a monad of certain type parameter into a monad of another.</li>
 * </ul>
 *
 * <p>Operations must be satisfy, the monads laws:</p>
 * <ul>
 * <li>left-identity  => unit(x).flatMap(f) == f(x)</li>
 * <li>right-identity => aMonadInstance.flatMap(unit) == aMonadInstance</li>
 * <li>associativity  => m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))</li>
 * </ul>
 */
object Monads extends App {

  //Try Monad

  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Fail(throwable: Throwable) extends Attempt[Nothing] {
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  /*

  left-identity

  unit.flatMap(f) = f(x)
  Attempt(x).flatMap(f) = f(x) //Success case!
  Success(x).flatMap(f) = f(x) //proved

  right-identity

  attempt.flatMap(unit) = attempt
  Success(x).flatMap(x => Attempt(x)) = Attempt(x) = Success(x)
  Fail(e).flatMap(...) = Fail(e)

  associativity

  attempt.flatMap(f).flatMap(g) = attempt.flatMap(x => f(x).flatMap(g))
  Fail(e).flatMap(f).flatMap(g) = Fail(e)
  Fail(e).flatMap(x => f(x).flatMap(g)) = Fail(e)

  Success(v).flatMap(f).flatMap(g) =
    f(v).flatMap(g) OR Fail(e)

  Success(v).flatMap(x => f(x).flatMap(g)) =
    f(v).flatMap(g) OR Fail(e)

  */

  val attempt = Attempt {
    throw new RuntimeException("My own monad!")
  }
  println(attempt)

  //Lazy Monad

  class Lazy[+A](value: => A) {
    private lazy val internalValue = value
    def use: A = internalValue
    def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(internalValue)
  }
  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy(value)
  }

  val lazyInstance = Lazy {
    println("Today I dont feel so well")
    42
  }
  val flatMapInstance = lazyInstance.flatMap(x => Lazy {
    10 * x
  })
  val flatMapInstance2 = lazyInstance.flatMap(x => Lazy {
    10 * x
  })
  flatMapInstance.use
  flatMapInstance2.use

  /*
  left-identity

  unit.flatMap(f) = f(v)
  Lazy(v).flatMap(f) = f(v)

  right-identity

  l.flatMap(unit) = l
  Lazy(v).flatMap(x => Lazy(x)) = Lazy(v)

  associativity

  l.flatMap(f).flatMap(g) = l.flatMap(x => f(x).flatMap(g))
  Lazy(v).flatMap(f).flatMap(g) = f(v).flatMap(g)
  Lazy(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g)
   */



}
