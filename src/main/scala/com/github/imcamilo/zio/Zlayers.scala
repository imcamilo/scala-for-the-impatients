package com.github.imcamilo.zio

import zio.{ExitCode, Has, Task, URIO, ZIO, ZLayer}

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
   * provides you some functions that will provide Zio instances for descriptions of affects that will actually happens
   * if you trigger them
   */
  import zio.console._
  val greeting = for { //ZIO[Console, IOException, Unit]
    _ <- putStrLn("Hi, what's your name?")
    name <- getStrLn
    _ <- putStrLn(s"Hello $name, welcome to ZIO")
  } yield () //yielding unit, dont care about the value of this computation

  /**
   * Creating heavy apps involve services:
   * - interacting with store layer
   * - bussines logic
   * - front-facing APIs e.g. through HTTP
   * - comunication with other services
   */

  case class User(name: String, email: String)

  object UserEmailer {

    type UserEmailerEnv = Has[UserEmailer.Service]

    // 1. service definition
    trait Service {
      //async computation returning a particular kind of value like a future
      def notify(user: User, message: String): Task[Unit] //ZIO[Any, Throwable, Unit]
    }

    /**
     * 2. Service implementation
     * We can treat the creation of the service as an effect and we can wrap it inside an effectfull creation of the service
     * This is zlayer that contains the real implementation of this service
     */
    val live: ZLayer[Any,Nothing, UserEmailerEnv] = ZLayer.succeed(new Service {
      override def notify(user: User, message: String): Task[Unit] = Task {
        println(s"[UserMailer] sending the message $message to ${user.email}")
      }
    })

    /**
     * 3. Front-facing API
     * accessM will be able to fetch the instance that is provided here as the input type.
     * It will fetch the instance of the environment -> Emailer.Service
     */
    def notify(user: User, message: String): ZIO[UserEmailerEnv, Throwable, Unit] =
      ZIO.accessM(hasService => hasService.get.notify(user, message))

  }

  //exact same pattern
  object UserDB {

    type UserDBEnv = Has[UserDB.Service]

    trait Service {
      def insert(user: User): Task[Unit]
    }

    val live: ZLayer[Any, Nothing, UserDBEnv] = ZLayer.succeed(
      (user: User) => Task {
        println(s"[UserDB] inserting ${user.email} in database")
      }
    )

    def insert(user: User): ZIO[UserDBEnv, Throwable, Unit] =
      ZIO.accessM(_.get.insert(user))

  }

  //HORIZONTAL composition
  //ZLayer[In1, E1, Out1] ++ ZLayer[In2, E2, Out2] => ZLayer[In1 with In2, super(E1, E2), Out1 with Out2]

  import UserDB._
  import UserEmailer._
  val userBackendLayer: ZLayer[Any, Nothing, UserDBEnv with UserEmailerEnv] = UserDB.live ++ UserEmailer.live

  //VERTICAL composition
  object UserSuscription {

    type UserSuscriptionEnv = Has[UserSuscription.Service]

    class Service(notifier: UserEmailer.Service, userDB: UserDB.Service) {
      def suscribe(user: User): Task[User] =
        for {
          _ <- userDB.insert(user)
          _ <- notifier.notify(user, s"composing vertically using ZIO - ${user.email}")
      } yield user
    }

    val live: ZLayer[Has[UserEmailer.Service] with Has[UserDB.Service], Nothing, UserSuscriptionEnv] =
      ZLayer.fromServices[UserEmailer.Service, UserDB.Service, UserSuscription.Service] {
        (userEmailer, userDB) => new Service(userEmailer, userDB)
      }

    def suscribe(user: User): ZIO[UserSuscriptionEnv, Throwable, User] = ZIO.accessM(_.get.suscribe(user))

  }
  import UserSuscription._
  val userSuscriptionLayer: ZLayer[Any, Nothing, UserSuscriptionEnv] = userBackendLayer >>> UserSuscription.live

  val eren: User = User("eren", "eren@aot.com")
  val message = "getting started with ZIO"

  /**
   * Zeo instances are just descriptions of effects, so I need to extends zio.App and overrite this method
   * With exitCode the effects are actually be performed and the value of this computation will be returned
   * as the return value of this run method
   */
  def oldRunNotified = {
    //greeting.exitCode
    UserEmailer.notify(eren, message) //the kind of effect
      //.provideLayer(UserEmailer.live) //provide the input for that effect
      .provideLayer(userBackendLayer) //provide the input for that effect
      .exitCode

    val combining = for {
      _ <- UserDB.insert(eren)
      _ <- UserEmailer.notify(eren, message)
    } yield ()
    combining.provideLayer(userBackendLayer)
      .exitCode

    ()

  }
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
   UserSuscription.suscribe(eren)
     .provideLayer(userSuscriptionLayer)
     .exitCode

  }

}
