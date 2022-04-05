package com.github.imcamilo.review.concurrency

//JVM Concurrency Problems
object Problems {

  def runInParallel(): Unit = {
    var x = 0
    val thread1 = new Thread(() => {
      x = 1
    })
    val thread2 = new Thread(() => {
      x = 2
    })
    thread1.start()
    thread2.start()
    println(x) //race condition
  }

  case class BankAccount(var amount: Int)

  def buy(bankAccount: BankAccount, thing: String, price: Int): Unit = {
    bankAccount.amount -= price
  }

  def buySafe(bankAccount: BankAccount, thing: String, price: Int): Unit = {
    bankAccount.synchronized { //does not allow multiple threads to run the critical section at the same time
      bankAccount.amount -= price //critical section
    }
  }

  def demoBankingProblem(): Unit = {
    (1 to 10000).foreach { _ =>
      val account = BankAccount(50000)
      val thread1 = new Thread(() => buy(account, "shoes", 3000))
      val thread2 = new Thread(() => buy(account, "iPhone", 4000))
      thread1.start()
      thread2.start()
      thread1.join()
      thread2.join()
      if (account.amount != 43000) println(s"Agh! I have just broken the bank ${account.amount}")
    }
  }

  //- thread1 -> thread2 -> thread3 -> ... and prints value of thread3, thread2, thread1
  def nestedThreads(maxThreads: Int, i: Int = 1): Thread =
    new Thread(() => {
      if (i < maxThreads) {
        val newThread = nestedThreads(maxThreads, i + 1)
        newThread.start()
        newThread.join()
      }
      println(s"Hello from thread ${i}")
    })

  /*
  what the max/min value?
  max value = 100 - each thread increases the x by 1
  min value = 1 -
    all threads reads x = 0 at the same time
    all threads in parallel compute 0 + 1 = 1
    all threads try to write x = 1
   */
  def minMax(): Unit = {
    var x = 0
    val threads = (1 to 100).map(_ => new Thread(() => x += 1))
    threads.foreach(_.start())
  }

  /*
  sleep fallacy
  synchronization doesnt work here, because there is no race condition
  so synchronization only works when there are multiple threads competing
  for the same memory zones
  */
  def `lackOfSynchronization?`(): Unit = {
    var message = ""
    val aweasomeThread = new Thread(() => {
      Thread.sleep(1000)
      message = "Scala rocks!"
    })
    message = "Scala sucks"
    aweasomeThread.start()
    Thread.sleep(1001)
    aweasomeThread.join() //solution - join the worker thread
    println(message)
  }

  def main(args: Array[String]): Unit = {
    //runInParallel()
    //demoBankingProblem()
    //nestedThreads(50).start()
    `lackOfSynchronization?`()
  }

}
