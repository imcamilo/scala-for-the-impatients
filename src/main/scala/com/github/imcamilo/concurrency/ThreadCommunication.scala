package com.github.imcamilo.concurrency

import scala.collection.mutable
import scala.util.Random

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

  //smarterProducerConsumer()

  /*
    producer -> [ ? ? ? ] -> consumer
   */
  def largeBufferProducerConsumer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer = new Thread(() => {
      val random = new Random()
      while (true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("[consumer] buffer empty, waiting...")
            buffer.wait()
          }
          //there must be at least ONE value in the buffer
          val x = buffer.dequeue()
          println("[consumer] consumed " + x)
          //hey producer, there is an empty space available, are you lazy?
          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0
      while (true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            println("[producer] buffer is full, waiting...")
            buffer.wait()
          }
          //there must be at least ONE EMPTY SPACE in the buffer
          println("[producer] producing " + i)
          buffer.enqueue(i)
          //hey consumer, new food for you
          buffer.notify()
          i += 1
        }
        Thread.sleep(random.nextInt(500))
      }
    })

    consumer.start()
    producer.start()
  }

  //largeBufferProducerConsumer

  /*
    producer1 -> [ ? ? ? ] -> consumer1
    producer2 -----^   ^----- consumer2
   */

  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      while (true) {
        buffer.synchronized {
          while (buffer.isEmpty) {
            println(s"[consumer $id] buffer empty, waiting...")
            buffer.wait()
          }
          val x = buffer.dequeue()
          println(s"[consumer $id] consumed " + x)
          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    }
  }

  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      var i = 0
      while (true) {
        buffer.synchronized {
          while (buffer.size == capacity) {
            println(s"[producer $id] buffer is full, waiting...")
            buffer.wait()
          }
          println(s"[producer $id] producing " + i)
          buffer.enqueue(i)
          buffer.notify()
          i += 1
        }
        Thread.sleep(random.nextInt(500))
      }
    }
  }

  def multiBufferProducerAndConsumer(nConsumers: Int, nProducers: Int): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 20
    (1 to nConsumers).foreach(a => new Consumer(a, buffer).start())
    (1 to nProducers).foreach(a => new Producer(a, buffer, capacity).start())
  }

  multiBufferProducerAndConsumer(3, 3)

}
