package com.github.imcamilo.review.patternmatching

import com.github.imcamilo.review.collection.IMListLatest.{Cons, Empty, IMList}

object AllThePatterns extends App {

  //1 - Constants
  val x: Any = "Scala"
  val constants = x match {
    case 1 => "a number"
    case "Scala" => "The Scala Language"
    case true => "The truth"
    case AllThePatterns => "A singleton object"
  }

  //2 - Match Anything
  //2.1 wildcard
  val matchAnything = x match {
    case _ =>
  }

  //2.2 variable
  val matchVariable = x match {
    case something => s"I've found $something"
  }

  //3 - tuples
  val aTuple = (1, 2)
  val matchTuple = aTuple match {
    case (1, 1) =>
    case (something, 2) => s"I've found $something"
  }

  val nestedTuple = (1, (2, 3))
  val matchNestedTuple = nestedTuple match {
    case (_, (2, v)) =>
  }
  //PMs can be nested

  //4 - case classes - constructor pattern
  //PMs can be nested with Case Classes as well
  val imList: IMList[Int] = Cons(1, Cons(2, Empty))
  val matchIMList = imList match {
    case Empty =>
    case Cons(head, tail) =>
    case Cons(head, Cons(subHead, tail)) =>
  }

  //5 - list patterns
  val aStandardList = List(1, 2, 3, 42)
  val matchStandardList = aStandardList match {
    case List(1, _, _, _) => //extractor - advanced
    case List(1, _*) => //list of arbitrary length - advanced
    case 1 :: List(_) => //infix pattern
    case List(1, 2, 3) :+ 42 => //infix pattern
  }

  //6 - type specifiers
  val unknown: Any = 2
  val matchUnknown = unknown match {
    case list: List[Int] => //explicit type specifier
    case _ =>
  }

  //7 - name binding
  val matchNameBinding = imList match {
    case nonEmptyList@Cons(_, _) => //name binding => use the name later (here)
    case Cons(1, rest@Cons(hd, tl)) => //name binding inside nested patterns
  }

  //8 - multi-patterns
  val matchMultiPattern = imList match {
    case Empty | Cons(0, _) => //compound patterns (multi-pattern)
    case _ => //for avoiding MatchError
  }

  //9 - if guards
  val matchSecondElementSpecial = imList match {
    case Cons(_, Cons(specialElement, _)) if specialElement % 2 == 0 =>
  }

  //ALL
  val numbers = List(1, 2, 3)
  //what would be the match?
  val matchNumbers = numbers match {
    case listOfStrings: List[String] => "a list of strings"
    case listOfInt: List[Int] => "a list of ints"
    case _ =>
  }
  println(matchNumbers)
  //JVM Trick question
  //backward compatibility JVM
  //Java 1 - Java 5 added generics -> all patterns looks like List
  //it is called TYPE ERASURE

}
