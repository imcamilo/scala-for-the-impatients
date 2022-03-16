package com.github.imcamilo.implicits

object PimpMyLibrary extends App {

  //Enrichments allows us to decorate existing classes that we may not have access to with additional methods and props
  //this would be nice:
  //2.isPrime

  //implicit class must take only one and only argument
  //this pattern often extends AnyVal for memory optimizations
  implicit class RichInt(val value: Int) extends AnyVal {
    def isEven: Boolean = value % 2 == 0
  }

  new RichInt(8766).isEven
  8766.isEven //new RichInt(8766).isEven
  //type enrichment == pimping

  1 to 10

  import scala.concurrent.duration._

  3.seconds

  //compiler doesn't do multiple implicit searches

  implicit class RicherInt(richInt: RichInt) {
    def isOdd: Boolean = richInt.value % 2 != 0
  }

  //8766.isOdd
  //wont compile, because the compiler attempts to expand or wrap 8766 into whatever conversions it can access
  //but it doesn't do another search to enrich those as well

  implicit class RichString(val str: String) extends AnyVal {
    def asInt: Int = Integer.parseInt(str)

    def encrypt(cypherDistance: Int): String = str.map(c => (c + cypherDistance).asInstanceOf[Char])
  }

  println("8766 as int -> " + "8766".asInt)
  println("John encrypted -> " + "John".encrypt(2))

  implicit class RicherInt2(val int: Int) extends AnyVal {
    def times(f: () => Unit): Unit = {
      def timesAux(n: Int): Unit = {
        if (n <= 0) ()
        else {
          f()
          timesAux(n - 1)
        }
      }

      timesAux(int)
    }

    def *[A](list: List[A]): List[A] = {
      def concatenate(n: Int): List[A] = {
        if (n <= 0) List()
        else concatenate(n - 1) ++ list
      }

      concatenate(int)
    }
  }

  3.times(() => println("gg wp"))
  println(4 * List(1, 2))

  //"3" / 4 it can work with implicit conversions
  implicit def strToInt(str: String): Int = Integer.parseInt(str)
  println("6" / 3) //rewrites as strToInt("6") / 3

  //equivalent: implicit class RichAltInt(value: Int)
  class RichAltInt(value: Int)
  implicit def enrich(value: Int): RichAltInt = new RichAltInt(value)

  //danger zone
  implicit def intToBoolean(i: Int): Boolean = i == 1

  val conditionedValue = if (8766) "OK" else "Something wrong!"
  println(conditionedValue)

  //pimp library responsibly
  //keep type enrichment to implicit classes and type classes
  //avoid implicit defs as much as possible
  //package implicit clearly, bring into scope only when you need
  //if you need conversions, make them specific

}
