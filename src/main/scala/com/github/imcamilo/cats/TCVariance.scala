package com.github.imcamilo.cats

object TCVariance {

  // Eq[Int] TC instance
  import cats.instances.int._
  // construct an Eq[Option[Int]] TC instance
  import cats.instances.option._
  import cats.syntax.eq._

  val aComparison = Option(2) === Option(3)
  // val anInvalidComparison = Some(2) === None // Eq[Some[Int]] not found
  // this is related to variance

  // variance
  class Animal
  class Cat extends Animal

  // covariant type... by creating a class or trait, adding + to the generic type.
  // subtyping is propagated to the generic type
  class Cage[+C]
  val cage: Cage[Animal] = new Cage[Cat] // cat <: animal, so Cage[Cat] <: Cage[Animal]

  // contravariant
  // subtyping is propagated backwards to the generic types
  class Vet[-C]
  val vet: Vet[Cat] = new Vet[Animal] // Cat <: Animal, then Vet[Animal] <: Vet[Cat]

  // RULE OF THUMB:
  // "HAS A T" = COVARIANT
  // "ACTS ON T" = CONTRAVARIANT

  // variance affect how TC instances are beign fetched

  // CONTRAVARIANT TC
  trait SoundMaker[-C] // 1
  implicit object AnimalSoundMaker extends SoundMaker[Animal] // 2
  def makeSound[C](implicit soundMaker: SoundMaker[C]) = println("hey") // 3

  makeSound[Animal] // ok. TC instance defined above
  makeSound[Cat] // ok. TC instance Animal is also applicable to Cats

  // RULE 1. CONTRAVARIANT TCS CAN USE THE SUPERCLASS INSTANCE IF NOTHING IS AVAILABLE STRICTLY FOR THAT TYPE

  // has implications for subtypes
  implicit object ObjectSoundMaker extends SoundMaker[Option[Int]]
  makeSound[Option[Int]]
  makeSound[Some[Int]]
  /// that works, but Eq, is not contravariant, is invariant

  // COVARIANT TC
  // 1
  trait AnimalShow[+C] {
    def show: String
  }
  // 2
  implicit object GeneralAnimalShow extends AnimalShow[Animal] {
    override def show: String = "animals everywhere"
  }
  implicit object CatsAnimalShow extends AnimalShow[Cat] {
    override def show: String = "cats everywhere"
  }
  // 3
  def organizeShow[C](implicit event: AnimalShow[C]): String = {
    event.show
  }

  // RULE 2. COVARIANT TCS, WILL ALWAYS USE THE MOST SPECIFIC TC INSTANCE FOR THAT TYPE
  // BUT MAY CONFUSE THE COMPILE IF THE GENERAL TC IS ALSO PRESENT.

  // RULE 3. YOU CANT HAVE BOTH BENEFITS
  // CATS USES INVARIANT TCs

  // so for comparison in Options and Empty options... dont use None...
  val comparisonOptionHappy = Option(1) === Option.empty[Int]

  def main(args: Array[String]): Unit = {
    // use
    println(organizeShow[Cat]) // ok, compiler will inject CatsShow as implicit
    // println(organizeShow[Animal]) // will not compile - ambigous values bcs we have 2 TC instances Animal the general and the subtype
  }
}
