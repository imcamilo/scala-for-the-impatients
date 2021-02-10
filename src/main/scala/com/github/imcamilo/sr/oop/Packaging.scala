package com.github.imcamilo.sr.oop

//use _ only when you need it

import com.github.imcamilo.sr.basics.{Recursion, SmartStrings, DefaultArgs => DefaultsArguments}

import java.util.Date
import java.sql.{Date => SqlDate}

object Packaging extends App {

  //package members are accessible by their simple names
  val jjh = new Writer("", "", 3)

  //import the package
  val sstrings = SmartStrings //com.github.imcamilo.gstarted.basics.SmartStrings //fully qualified name

  //package are in hierarchy
  //matching folder structure

  //package object
  greet()
  println(SPEED_OF_LIGHT)

  //imports
  val recursion = Recursion
  //alias
  val defaultArgs = DefaultsArguments

  //1. Use QF names
  val date = new Date()
  //val sqlDate = new java.sql.Date(202102101625)
  //2. Use Alias
  val sqlDate = new SqlDate(2002101625)

  //default imports
  //java.lang - String, Object, Exception
  //scala - Int, Nothing, Function
  //scala.Predef - println, ???

}
