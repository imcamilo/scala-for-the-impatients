package com.github.imcamilo.typesystem

object PathDependentTypes extends App {

  //nested types and how they're used and accessed

  class Outer {
    class Inner
    object InnerObject
    type InnerType
    def print(i: Inner): Unit = println(i)
    def printGeneral(i: Outer#Inner): Unit = println(i)
  }

  def aMethod: Int = {
    class HelperClass
    type HelperType = String
    2
  }

  //per-instance
  val outer = new Outer
  val inner = new outer.Inner //outer.Inner is a Type

  val otherOuter = new Outer
  //val otherInner: otherOuter.Inner = new outer.Inner //outer.Inner is a Type // NOT OK

  outer.print(inner)
  //otherOuter.print(inner) NOT OK

  //This is called Path-Dependent Type

  //The fact that all the inner types have a common super type and that is
  //Outer#Inner
  outer.printGeneral(inner)
  otherOuter.printGeneral(inner)

  /*
  db keyed by int or string, but maybe others
  use path dependent types and type members/type aliases
   */
  trait ItemLike {
    type Key
  }
  trait Item[K] extends ItemLike {
    type Key = K
  }
  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  def get[ItemType <: ItemLike](value: ItemType#Key): ItemType = ???

  get[IntItem](87655) //OK
  get[StringItem]("87655") //OK
  //get[IntItem]("Scala") //NOT OK

}
