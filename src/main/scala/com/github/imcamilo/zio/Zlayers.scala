package com.github.imcamilo.zio

import zio.{ExitCode, URIO, ZIO}

object Zlayers extends zio.App {

  /**
   * Zio library's center is ZIO type
   * Zio instances are called "Effects" which
   * describes anything that a program does (computing, printing, opening, sockets, etc)
   *
   * A effects is caused by an input and can produce Either Error or the desired value
   *
   * Zio receive 3 type arguments
   * - input type which is called R which is known as the environment
   * - error type which is called E
   * - value type which is called A
   * -
   * ZIO[-R, +E, +A]
   *
   * This instances of Zio are conceptually equivallent to a function from
   * R => Either[E, A]
   *
   */

  val meaningOfLife = ZIO.succeed(8766) //this is an effect this is a computation producing a pure value
  val aFailure = ZIO.fail("Something went wrong") //failure

  /**
   * For real effects like printing something in a console are also described by Zio
   */

  //provides you some functions that will provide Zio instances for descriptions of affects that will actually happens
  //if you trigger them
  import zio.console._
  val greeting = for { //ZIO[Console, IOException, Unit]
    _ <- putStrLn("Hi, what's your name?")
    name <- getStrLn
    _ <- putStrLn(s"Hello $name, welcome to ZIO")
  } yield () //yielding unit, dont care about the value of this computation

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    /*
     Zeo instances are just descriptions of effects, so I need to extends zio.App and overrite this method
     With exitCode the effects are actually be performed and the value of this computation will be returned
     as the return value of this run method
     */
    greeting.exitCode
  }

}
