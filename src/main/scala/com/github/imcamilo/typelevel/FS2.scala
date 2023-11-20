package com.github.imcamilo.typelevel

object PureStreams extends App {
  import fs2._

  // much like List() constructor
  // pure streams don't have any side effects.
  val pureStream: Stream[Pure, Int] = Stream(1, 2, 3, 4) // fs2 package

  // Streams can be "compiled" to another data structure
  val listd: List[Int] = pureStream.compile.toList

  // Streams have list like operations that works like you would expect
  val a: Stream[Pure, String] = Stream("John", "Ringo")
  val b: Stream[Pure, String] = Stream("George", "Paul")

  a ++ b // new streams, just like lists
  println((a ++ b).compile.toVector)

}

/** object EffectfullStreams extends App { import cats.effect.IO import fs2._
 *
 *  val stream = Stream("John", "Ringo", "George", "Paul")
 *
 *  def getAge(n: String): IO[Int] = IO(n.length)
 *
 *  // effectul equivalent of map val effectfullTransformed: Stream[IO, Int] = stream.evalMap(getAge) val
 *  effectfullFiltered = stream.evalFilter(name => IO(name.contains("o")))
 *
 *  effectfullTransformed.compile.toList.unsafeRunSync()
 *
 *  }
 */
