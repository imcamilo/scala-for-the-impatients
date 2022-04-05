package com.github.imcamilo.review.implicits

//how the compiler looks for implicit and where, and which implicits have priority over others
object OrganizingImplicits extends App {

  //implicit def reverseOrdered   - OK
  //implicit def reverseOrdered() - NOT OK
  implicit val reverseOrdered: Ordering[Int] = Ordering.fromLessThan(_ > _)
  //wont compile because we have 2 implicit values and the compiler is confused
  //because the compiler doesn't know which implicit value to use
  //implicit val normalOrdered: Ordering[Int] = Ordering.fromLessThan(_ < _)
  println(List(1, 5, 3, 2, 0).sorted)

  //scala.Predef

  /*
  Implicits (used as implicit parameters):
  - val/var
  - objects
  - access/methods = defs with no parentheses
   */

  //implicit ordering for person
  case class Person(name: String, age: Int)

  val people = List(Person("Grisha", 40), Person("Eren", 26), Person("Zeke", 36))

  /*
  Implicits Scope
  - normal scope = local scope
  - imported scope
  - companion of all types involved in the method signature (object Person)

  - ex: override def sorted[B >: A](implicit ord: Ordering[B]): List[B]
  - the compiler search in:
    - List type
    - Ordering type
    - All the types involved = A or any supertype (Person)
   */

  /*
  When defining a implicit val

  #1
  - if there is a single possible value for it
  - and you can edit the code for the type
  Then define the implicit in the companion

  #2
  - if there are many possible values for ti
  - but a single good one for most of the cases
  - and you can edit the code for the type
  Then define the good implicit in the companion and the other implicit elsewhere, prefer in either the local scope or in other objects

   */

  object AlphabeticOrdering {
    implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }

  object AgeOrdering {
    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age < b.age)
  }

  import AgeOrdering._

  println(people.sorted)

  //
  case class Purchase(nUnits: Int, unitPrice: Double)

  object Purchase {
    implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.nUnits * a.unitPrice < b.nUnits * b.unitPrice)
  }

  object UnitCountOrdering {
    implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.nUnits < b.nUnits)
  }

  object UnitPriceOrdering {
    implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.unitPrice < b.unitPrice)
  }

}
