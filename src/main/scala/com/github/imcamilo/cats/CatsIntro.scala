package com.github.imcamilo.cats

object CatsIntro extends App {

  // Eq simple type class that compiles values at compile time

  // checking this. expresion and its false
  // val aComparison = 2 == "a string"

  // 1. TYPE CLASS IMPORT
  import cats.Eq

  // 2. IMPORT TC INSTANCES FOR THE TYPES YOU NEED
  import cats.instances.int._

  // 3. USE THE TC API
  val intEquality = Eq[Int]
  val aTypeSafeComparison = intEquality.eqv(2, 3) // false
  // val aUnsafeComparison = intEquality.eqv(2, "") // doesn't compile

  // 4. USE EXTENSION METHODS (IF APPLICABLE) ... GIVE ME ALL THE EXTENSIONS METHDOS AVAILABLE IN THAT TC
  import cats.syntax.eq._
  val anotherTypesafeComparison = 2 === 3 // false
  val notEqualComparison = 2 =!= 3 // true
  // val invalidComparison = 2 === "" // doesn't compile
  // extension methods are only visible in the presence of the right TC instance

  // this is the general pattern on using a TC in cats
  /*
   * 1. import TC definition
   * 2. import TC instances for the types that we wanna support
   * 3. use the TC API or extension methods
   */

  // 5. EXTENDING THE TC OPERATIONS TO COMPOSITE TYPES, E.G. LISTS.
  import cats.instances.list._ // we bring eq of Eq[List[Int]] in scope
  val aListComparison = List(2) === List(3) // false

  // 6. CREATE A TYPE CLASS INSTANCE FOR A CUSTOM TYPE
  case class ToyCar(model: String, price: Double)

  implicit val ToyCarEq: Eq[ToyCar] = Eq.instance[ToyCar] { (car1, car2) => car1.price == car2.price }
  val compareTwoToyCars = ToyCar("Ferrari", 42313) === ToyCar("McLaren", 423123)

}
