package com.github.imcamilo.concurrency

object ThreadCommunication extends App {

  //the producer-consumer problem
  //produce -> [ x ] -> consumer

  class Container {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0

    def set(newValue: Int): Unit = value = newValue

    def get: Int = { //"consuming method"
      val result = value
      value = 0
      result
    }
  }

  def simulateConsumerProducer(): Unit = {
    val container = new Container

    val consumer = new Thread(() => {
      println("[consumer] - waiting...")
      while (container.isEmpty) {
        println("[consumer] - actively waiting...")
      }
      println("[consumer] - I have consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] - computing...")
      Thread.sleep(500)
      val value = 42
      println("[producer] - I have produced, after a long work, the value " + value)
      container.set(value)
    })

    consumer.start()
    producer.start()

  }

  //simulateConsumerProducer()

  /*
  Only AnyRefs can have synchronized blocks - int, boolean do not have synchronized expressions
  synchronized locks the object's monitor
  (Monitor is a data structure internally used by the JVM to keep track which object is locked by which thread)
  someObject.synchronized {   //synchronized locks the object's monitor
    code                      //any other thread trying to run this will block
  }                           //release the lock

  General Principles:
  - make no assumptions about who gets the lock first
  - keep locking to a minimum
  - maintain thread safety at ALL time in parallel applications

  wait() and notify() only allowed inside the synchronized expressions

  wait() -ing -> on an object's monitor suspend the thread indefinitely
  - thread1
  someObject.synchronized {   //locks the object's monitor
    code part 1               //any other thread trying to run this will block
    someObject.wait()         //release the lock monitor and suspend the thread at this point
    code part 2               //when allowed to proceed, it will take the lock of the monitor again and continue evaluating code
  }                           //release the lock
  - thread 2
  someObject.synchronized {   //locks the object's monitor
    code
    someObject.notify()       //it will give the signal to one of the sleeping threads that are waiting this objects's monitor
                              //(which thread? I don't know) at the discresion of the JVM and of the OS
                              //if you want to signal all the running threads that they may continue, use notifyAll()
    more code
  }                           //but only after I'm done and unlock the monitor



  */

  def smarterProducerConsumer(): Unit = {
    val container = new Container
    val consumer = new Thread(() => {
      println("[consumer] - waiting...")
      container.synchronized {
        container.wait()
      }
      //container must have some value
      println("[consuemr] - I have consumed " + container.get)
    })
    val producer = new Thread(() => {
      println("[producer] - Hard at work...")
      Thread.sleep(2000)
      val value = 42
      container.synchronized {
        println("[producer] - I'm producing " + value)
        container.set(value)
        container.notify()
        println("[producer] - notifying " + value)
      }
    })
    consumer.start()
    producer.start()
  }

  smarterProducerConsumer()

}
