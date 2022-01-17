package com.github.imcamilo.review.oop

object MethodNotation extends App {

  class Person(val name: String, favoriteMovie: String, val age: Int = 0) {
    def likes(movie: String): Boolean = movie == favoriteMovie
    def hangOutWith(person: Person): String = s"${this.name} is hanging out with ${person.name}"
    def +(person: Person): String = s"${this.name} is hanging out with ${person.name}"
    def +(nick: String) : Person = new Person(s"$name ($nick)", favoriteMovie)
    def unary_+(nick: String) : Person = new Person(s"$name ($nick)", favoriteMovie)
    def unary_+() : Person = new Person(name, favoriteMovie, age + 1)
    def unary_! : String = "what the f***!"
    def isAlive: Boolean = true
    def learns(lang: String) = s"$name is learning $lang"
    def learnScala: String = this learns "Scala"
    def apply(): String = s"Hi, my name is $name and I like $favoriteMovie"
    def apply(i: Int): String = s"$name watched $favoriteMovie $i times"
  }

  val mary = new Person("Mary","Inception")

  println(mary.likes("Inception"))

  //INFIX NOTATION = OPERATOR NOTATION (SYNTACTIC SUGAR) //ONLY WORKS WITH ONLY ONE PARAM METHODS
  println(mary likes "Inception") //equivalent

  //OPERATOS IN SCALA
  val tom = new Person("Tom", "LoTR")
  println(tom hangOutWith mary)
  println(mary + tom)
  println(mary.+(tom))

  //ALL OPRATORS ARE METHODS
  println(1 + 2)
  println(1.+(2))
  //Akka actos have ! ?

  //PREFIX NOTATIONS
  val x = -1 //equivalent 1.unary_-
  val y = 1.unary_-
  //unary_ prefix only works with - + ~ !
  println(!mary)
  println(mary.unary_!)

  //POSTFIX NOTATIONS //IS ONLY AVAILABLE ON METHODS WITHOUT PARAMETERS
  println(mary.isAlive)
  println(mary isAlive)

  //APPLY
  println(mary.apply())
  println(mary()) //equivalent

  //1. Overload the + operator
  println(mary.unary_+("the rockstar").apply())
  println((mary + "the rockstar")())

  //2. +mary increment age
  println((+mary).age)

  //3. add learns methdo, postfix notation
  println(mary learnScala)

  //4. overload apply
  println(mary(3))

}
