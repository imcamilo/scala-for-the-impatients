package com.github.imcamilo.sr.basics

object CallByNameAndByValue extends App {

  //THE EXACT VALUE OF THIS EXPRESION (c) IS COMPUTED BEFORE THE FUNCTION EVALUATES,
  //THE SAME VALUE IS USED IN THE FUNCTION DEFINITION
  def calledByValue(c: Long): Unit = {
    println("by value: " + c)
    println("by value: " + c)
  }

  //THIS EXPRESSION IS PASS LITERALLY AS IS, THATS WHY ITS CALLED BY NAME AND THE
  //EXPRESSION IS EVALUATED EVERY TIME
  def calledByName(d: => Long): Unit = {
    println("by name: " + d)
    println("by name: " + d)
  }

  calledByValue(System.nanoTime())
  calledByName(System.nanoTime())

  def infiniteRec(): Int = 1 + infiniteRec()
  def printFirst(x: Int, y: => Int) = println(x)

  //printFirst(infiniteRec(), 34) //dont work, stackoverflow
  printFirst(34, infiniteRec()) //it works, lazy evaluation

}
