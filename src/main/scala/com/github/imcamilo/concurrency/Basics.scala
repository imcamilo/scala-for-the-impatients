package com.github.imcamilo.concurrency

import java.util.concurrent.Executors

//JVM Threads
object Basics extends App {

  val runnable = new Runnable {
    override def run(): Unit = println("running in parallel")
  }
  val aThread = new Thread(runnable) //create a JVM Thread => OS Thread
  aThread.start() //gives the signal to the JVM to start a JVM thread
  runnable.run() //doesn't do anythign in parallel!
  aThread.join() //blocks until aThread finished running

  val aThreadHello = new Thread(() => (1 to 5).foreach(_ => println("Hello")))
  val aThreadGoodBye = new Thread(() => (1 to 5).foreach(_ => println("Goodby")))
  aThreadHello.start()
  aThreadGoodBye.start()
  //different runs produces differents results!

  //Executors /reuse threads
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("Something in the thread pool"))
  pool.execute(() => {
    Thread.sleep(1000)
    println("done after 1 second")
  })
  pool.execute(() => {
    Thread.sleep(1000)
    println("almost done")
    Thread.sleep(1000)
    println("done after 2 seconds")
  })

  pool.shutdown()
  //pool.execute(() => println("Should not appear"))
  //pool.shutdownNow()
  println(pool.isShutdown)
}
