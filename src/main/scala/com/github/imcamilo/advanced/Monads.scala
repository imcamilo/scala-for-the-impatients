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

  //my own Try monad

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


}
