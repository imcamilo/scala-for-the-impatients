package com.github.imcamilo.implicits

object GivensFor3 {

  /*
  implicit
  this works, but the order is important
  object Implicits {
    implicit val reverseOrdered: Ordering[Int] = Ordering.fromLessThan(_ > _)
  }
  val l = List(1, 5, 3, 2, 0).sorted
  the next code works on scala3 and the order doesn't matters
  object Givens {
    given reverseOrdered: Ordering[Int] = Ordering.fromLessThan(_ > _)
  }
  object GivensAnnonymousClassNaive {
    given reverseOrdered: Ordering[Int] = new Ordering[Int] {
      override def compare(x: Int, y: Int): Int = y - x
    }
  }
  //this is the proper way to define a bunch of method
  object GivensWith {
    given reverseOrdered: Ordering[Int] = with {
      override def compare(x: Int, y: Int): Int = y - x
    }
  }
  import GivensWith._               //in scala 3 this doesn't import given as well
  import GivensWith.given           //imports all givens
  import GivensWith.reverseOrdered  //an specific one


  Then:

  implicit vals <=> given clauses
  implicit arguments <=> using clauses
  implicit def (synthesize new implicit values)

  implicit conversions (abused in Scala 2) <=>
    import scala.language.implicitConversions
    use Conversion[FromType, ToType]
    use apply method
   */

}
