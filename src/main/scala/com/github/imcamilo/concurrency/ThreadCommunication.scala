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

  simulateConsumerProducer()

}
