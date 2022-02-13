package com.github.imcamilo.typesystem

object SelfTypes extends App {

  //selft types are a way tp requiring a type to be mixed in

  trait Instrumentalist {
    def play(): Unit
  }

  //SELF-TYPE
  trait Singer { self: Instrumentalist => //who ever implements Singer to implements Instrumentalist as well
    //rest of the implementation or API
    def sing(): Unit
  }

  class LeadSinger extends Singer with Instrumentalist {
    override def sing(): Unit = ???
    override def play(): Unit = ???
  }

  /* ILEGAL
  class Vocalist extends Singer {
    override def sing(): Unit = ???
  }
   */

  val james = new Singer with Instrumentalist {
    override def sing(): Unit = ???
    override def play(): Unit = ???
  }

  class Guitarist extends Instrumentalist {
    override def play(): Unit = println("guitar solo")
  }

  val ericClapton = new Guitarist with Singer {
     def sing(): Unit = ???
  }

  //vs inheritance
  class A
  class B extends A //B IS AN A

  trait T
  trait S { s: T => } //S REQUIRES T

  //CAKE PATTERN => java's "dependency injection"

  //Classically DI
  class Component {
    //SOME API
  }
  class ComponentA extends Component
  class ComponentB extends Component
  class DependentComponent(val component: Component)

  //CAKE PATTERN
  trait ScalaComponent {
    //SOME API
    def action(x: Int): String
  }
  trait ScalaDependentComponent { self: ScalaComponent =>
    def dependentAction(x: Int): String = action(x) + " this rocks"
  }
  trait ScalaApplication { self: ScalaDependentComponent => }

  //layer 1 - small components
  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent

  //layer 2 - compose components
  trait Profile extends ScalaDependentComponent with Picture
  trait Analytics extends ScalaDependentComponent with Stats

  //layer 3 - app
  trait AnalyticsApp extends ScalaApplication with Analytics

  /*
  DI -> the framework or some other piece of code takes care to verify or inject values at runtime
  Cake Pattern -> these dependencies are checks at compile time
   */

  /*
  self types allows usto define a seemingly cyclical dependcies
  illegal cyclic reference involving class X
  class X extends Y
  class Y extends X
   */
  trait X { self: Y => }
  trait Y { self: X => }


}
