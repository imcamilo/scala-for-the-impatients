package com.github.imcamilo.typelevel

/** Introduce to Values as Types
 */
object TypeLevelProgramming {

  import scala.reflect.runtime.universe._

  /** Boilerplate Will print the type of a value at Runtime
   *  @param value
   *  @param tag
   *  @tparam T
   *  @return
   */
  def show[T](value: T)(implicit tag: TypeTag[T]) =
    tag.toString().replace("com.github.imcamilo.typelevel.TypeLevelProgramming.", "")

  /*
   TYPE-LEVEL PROGRAMMING
   normal programming works with normal values which are concrete representations of some types in memory
   normal programming happens at runtime
   type-level will involve using the compiler as a program executor
   the compiler will infer some type constraints so some relationships between types that will mean the response to a
   mathematical problem
   */

  // natural numbers - we are able to represent natural numbers as types not as values
  // this is called Peano Arithmetic
  trait Nat
  class _0 extends Nat
  class Succ[N <: Nat] extends Nat

  // this are types - not are values
  type _1 = Succ[_0]
  type _2 = Succ[_1] // Succ[Succ[_0]]
  type _3 = Succ[_2]
  type _4 = Succ[_3]
  type _5 = Succ[_4]

  // compiler retriction
  // type Animal = Succ[Int]

  // now I want to do
  // _2 < _3?
  // less than
  trait <[A <: Nat, B <: Nat]
  object < {
    implicit def lessThanBasic[B <: Nat]: <[_0, Succ[B]] = new <[_0, Succ[B]] {}
    implicit def inductive[A <: Nat, B <: Nat](implicit lt: <[A, B]): <[Succ[A], Succ[B]] = new <[Succ[A], Succ[B]] {}
    def apply[A <: Nat, B <: Nat](implicit ltb: <[A, B]) = ltb
  }

  val comparison0and1: <[_0, _1] = <[_0, _1]
  val comparison1and3: _1 < _3 = <[_1, _3]
  /*
    <.apply[_1, _3]     -> requires an implicit   <[_1, _3]
    inductive[_1, _3]   -> requires implicit      <[_0, _2]
    lessThanBasic[_1]   -> produces implicit      <[_0, Succ[_1]] == <[_0, _2]
   */

  // val invalidComparison: _3 < _2 = <[_3, _3] //WILL NOT compile: 3 is not less than 2

  // less than or equal
  trait <=[A <: Nat, B <: Nat]
  object <= {
    implicit def lessThanEqualsBasic[B <: Nat]: <=[_0, B] = new <=[_0, B] {}
    implicit def inductive[A <: Nat, B <: Nat](implicit lte: <=[A, B]): <=[Succ[A], Succ[B]] =
      new <=[Succ[A], Succ[B]] {}
    def apply[A <: Nat, B <: Nat](implicit lte: <=[A, B]) = lte
  }

  val lessThanOrEqual1and1 = <=[_1, _1]
  //val invalidLessThanOrEqual1and1 = <=[_2, _1] //WILL NOT compile: 2 is not less or equal than 1

  def main(args: Array[String]): Unit = {
    println(show(List(1, 2, 3)))
    println(show(comparison0and1))
    println(show(comparison1and3))
  }

}
