package com.github.imcamilo.typesystem

object Variance extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  /*
  what is variance?
  problem of "inheritance" -> "" 'cause is the problem of type substitution of generics
  */

  //should a Cage[Cat] "inherit" from another Cage[Animal]?
  class Cage[C]

  //yes - covariance
  class CCage[+C] //covariance
  val ccage: CCage[Animal] = new CCage[Cat]

  //no - invariance
  class ICage[C] //invariance - i cant replace any type with cage with any other type of cage
  //val badICage: ICage[Animal] = new ICage[Cat]
  val icage: ICage[Animal] = new ICage[Animal]

  //hell no - opposite - contravariance
  class XCage[-C]
  val contraCage: XCage[Cat] = new XCage[Animal]

  //

  class InvariantCage[C](val animal: C) //invariant

  //covariant positions
  class CovariantCage[T](val animal: T) //COVARIANT POSITION

  /*
  Contravariant type C occurs in covariant position in type C of value animal
  class ContraVariantCage[-C](val animal: C)
  this would allow:
    val contraCage: XCage[Animal] = new XCage[Cat](new Crocodile) //this would be wrong for the types.

  Covariant type C occurs in contravariant position in type C of value animal
  class CovariantVariableCage[+C](var animal: C) //types of vars are in CONTRAVARIANT POSITION
  this would allow:
    val covariantCage: CCage[Animal] = new CCage[Cat](new Cat)
    covariantCage.animal = new Crocodile //this would be wrong for the types.


  Contravariant type C occurs in covariant position in type C of value animal
  class ContravariantVariableCage[-C](var animal: C) //also in COVARIANT POSITION
  this would allow:
    val covariantCage: XCage[Cat] = new CCage[Animal](new Crocodile)
  */

  class InvariantVariableCage[T](val animal: T) //OK

  //as we can saw, covariant and contravariance posititions are some compiler restrictions

  /*
  this wont compile
    trait AnotherCovariantCage[+C] {
      def addAnimal(animal: C) //contravariant position
    }
  this would be allow:
    val ccage: CCage[Animal] = new CCage[Cat]
    ccage.addAnimal(new Dog)
   */

  class AnotherContravariantCage[-C] {
    def addAnimal(animal: C) = true
  }
  val anotherContravariantCage: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  anotherContravariantCage.addAnimal(new Cat)
  class Misifu extends Cat
  anotherContravariantCage.addAnimal(new Misifu)

  class MyList[+C] {
    //using a supertype of C
    def add[B >: C](element: B): MyList[B] = new MyList[B] //widening the type
  }
  val emptyList = new MyList[Misifu]
  val animals = emptyList.add(new Misifu)
  val moreAnimals = animals.add(new Cat)
  val eventMoreAnimnals = moreAnimals.add(new Dog)

  //METHOD ARGUMENTS ARE IN CONTRAVARIANT POSITION

  //return types
  class PetShop[-C] {
    //def get(isItAPuppy: Boolean): C //METHOD RETURN TYPES IN A COVARIANT POSITION
    /*
    val catShop = new PetShop[Animal] {
      def get(isItAPuppy: Boolean): Animal = new Cat
    }
    val dogShop: PetShop[Dog] = catShop
    dogShop.get(true) //EVIL CAT
     */

    //using a subtype
    def get[S <: C](isItAPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }
  val shop: PetShop[Dog] = new PetShop[Animal]
  //do not conform to method get's type parameter bounds [S <: com.github.imcamilo.typesystem.Variance.Dog]
  //val evilCat = shop.get(true, new Cat)

  class TerraNova extends Dog
  val bigFurry = shop.get(true, new TerraNova)

  /*
  BIg rule
  - method arguments are in CONTRAVARIANT positions
  - return types are in COVARIANT position
   */

  /*
  1. invariant, covariant, contravariant parking
    Parking[C](things: List[C]) {
      def park(vehicle: C)
      def impound(vehicle: List[C])
      def checkVehicles(cond: String): List[C]
    }
  2. used someone else's API: IList[C]
  3. Parking = monad!
    - flatMap
   */

  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle
  class IList[C]

  //1
  //invariant
  class ParkingInvariant[C](things: List[C]) {
    def park(vehicle: C): ParkingInvariant[C] = ???
    def impound(vehicle: List[C]): ParkingInvariant[C] = ???
    def checkVehicles(cond: String): List[C] = ???
    //3
    def flatMap[B](f: C => ParkingInvariant[B]): ParkingInvariant[B] = ???
  }
  //covariant
  class ParkingCovariant[+C](things: List[C]) {
    def park[B >: C](vehicle: B): ParkingCovariant[B] = ???
    def impound[B >: C](vehicle: List[B]): ParkingCovariant[B] = ???
    def checkVehicles(cond: String): List[C] = ???
    //3
    def flatMap[B](f: C => ParkingCovariant[B]): ParkingCovariant[B] = ???
  }
  //contravariant
  class ParkingContravariant[-C](things: List[C]) {
    def park(vehicle: C): ParkingContravariant[C] = ???
    def impound(vehicle: List[C]) : ParkingContravariant[C]= ???
    def checkVehicles[B <: C](cond: String): List[B] = ???
    //3
    def flatMap[A <: C, B](f: A => ParkingContravariant[B]): ParkingContravariant[B] = ???
  }

  /*
    Rule of thumb
    - use covariance = COLLECTION OF THINGS
    - use contravariance = GROUP OF ACTIONS
   */


  //2
  //covariant
  class ParkingCovariant2[+C](things: IList[C]) {
    def park[B >: C](vehicle: B): ParkingCovariant2[B] = ???
    def impound[B >: C](vehicle: IList[B]): ParkingCovariant2[B] = ???
    def checkVehicles[B >: C](cond: String): IList[B] = ???
  }
  //contravariant
  class ParkingContravariant2[-C](things: IList[C]) {
    def park(vehicle: C): ParkingContravariant2[C] = ???
    def impound[B <: C](vehicle: IList[B]) : ParkingContravariant2[B]= ???
    def checkVehicles[B <: C](cond: String): IList[B] = ???
  }



}
