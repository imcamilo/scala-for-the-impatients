package com.github.imcamilo.review.typeclasses

object TypeClassesUtilties {

  /** Problem Specialized Implementation */

  trait Summable[A] {
    def sumElements(list: List[A]): A
  }

  implicit object IntSummable extends Summable[Int] {
    override def sumElements(list: List[Int]): Int = list.sum
  }

  implicit object StringSummable extends Summable[String] {
    override def sumElements(list: List[String]): String = list.mkString("")
  }

  def listProcessor[C](list: List[C])(implicit summable: Summable[C]): C = { //ad-hoc polymorphism

    /** Sum up all the elements on the list for integers => sum => actual sum of the elements for strings => sum =>
     *  concatenation of all the elements for other types => error
     */
    summable.sumElements(list) // <-- ad-hoc thing
  }

  def main(args: Array[String]): Unit = {
    val intValues = listProcessor(List(1, 2, 3, 4, 5, 6, 7))
    val strValues = listProcessor(List("Scala ", "Rocks!"))
    // listProcessor(List(true, false)) error at compile time
    println(intValues)
    println(strValues)
  }

  /**
   * AD-HOC POLYMORPHISM
   * the *summable.sumElements(list)* capability is unlock only in the presence of an implicit instance
   * of the trait which provides that method definition, in this case Summable[A].
   * Right when is called right here, so we require the presence of this implicit instance right at the
   * moment when we call this some elements method <-- so is the ad-hoc thing
   *
   * polymorphism because depending on the actual instance of this trait, the behavior of some elements
   * polymorphic and depends on the actual type C that's going to be used.
   *
   * That's how ad-hoc polymorphism got its name
   *
   * A trait with implicit instances... makes a pattern which we generally call a TYPE CLASS,
   * this structure allow us define specific implementations for certaint types and not for others and in
   * our case we provide implementation for Int and String and not for Anything else
   *
   */
}
