package com.github.imcamilo.monocle

/**
 * Interesting tool for manipulating data structures
 * This time we are working in the world of hierarchies
 * Usually sealed classes, traits o enums
 */
object Prisms extends App {

  sealed trait Shape
  case class Circle(radius: Double) extends Shape
  case class Rectangle(w: Double, h: Double) extends Shape
  case class Triangle(a: Double, b: Double, c: Double) extends Shape

  val aCircle = Circle(20)
  val aRectangle = Rectangle(10, 20)
  val aTriangle = Triangle(3, 4, 5)

  val aShape = aCircle
  //I.E. We would like to be able to increase the radius of this shape
  //And we'd like to increase its radius if it is a circle or leave it intact otherwise

  /**
   * first try
   * but this are usually pretty obnoxious, you wont see this in good quality code
   */
  if (aShape.isInstanceOf[Circle]) {
    //change the radius
  }

  /**
   * Whenever you need to apply this function or a modification to a Shape, that is defined to be the top of the
   * hierarchy you would need to resort to either type checking or pattern matching every time
   * You would need to repeat yourself all the type
   */
  val aShapeMatch = aShape match {
    case Circle(r) => Circle(r + 10)
    case _ =>
  }

  //PRISM
  import monocle.Prism

  /**
   * Prism takes two types arguments [Shape, Double]
   * Meaning giving a shape we can extract or access its Double which means Radius in our case
   * And we need two functions that'll turn a Shape into a Double
   * And backwards from a Double to a Shape
   *
   * 1. First function takes a Shape and returns a Option[Double]
   * 2. Second function takes a Double and returns a Option[Shape]
   *
   * In other words a prism will be a WRAPPER OVER A BACK AND FORTH TRANSFORMATION between a Shape and a Double
   * And a prism allows us to investigate a Shape and get a Double or use a Double and create a Shape
   */
  val circlePrism = Prism[Shape, Double] {
    case Circle(r) => Some(r)
    case _ => None
  }(r => Circle(r))

  //you can think of this application as some kind of smart constructor
  val anotherCircle = circlePrism(30)
  //whenever you say getOption the first function on Prism[Shape, Double] will run
  val radius = circlePrism.getOption(aCircle) //Some(20)
  val noRadius = circlePrism.getOption(aRectangle) //None because is not a Circle
  //we can use getOption on any kind of shape, we dont need type check anymore

  /**
   * Why is the pattern called Prism
   * Because we are working with a type hierarchy
   * Imagina a Prims with multiple Facets
   *  Either a circle, or a rectangle, or a triangle
   *
   *
   *
   * You can combine that with the lens pattern
   * And with a bunch of other optics patterns that monocle supports and gets some really nice effects
   * So probably the most powerful feature of Monocle is the ability to compose these patterns into other patterns
   * And we can inspect or modify nested data structures by combining the capabilities to zoom in (Lens)
   * And to isolate a type that is (Prism)
   */

  case class Icon(background: String, shape: Shape)
  case class Logo(color: String)
  case class BrandIdentity(logo: Logo, icon: Icon)

  /**
   * If you want to change the radius of an Icon of a Brand, assuming its a circle or leave it intact otherwise
   * We would create the appropiate accessors that is
   *  - Lenses: to ZOOM into the shape radius and
   *  - Prisms: modifieres for our desired type, in our case a Circle which is involving a prism
   */

  import monocle.macros.GenLens

  //given a BrandIdentity returns an Icon
  val iconLens = GenLens[BrandIdentity](_.icon)
  //given an Icon returns a Shape
  val shapeLens = GenLens[Icon](_.shape)

  //Now I will combine the iconLens with shapeLens with the circlePrism
  val brandCircleR = iconLens.andThen(shapeLens).andThen(circlePrism)
  //with all these in place, we can take some brands and apply some transformations

  val aBrand = BrandIdentity(Logo("Yellow"), Icon("Black", Circle(54)))
  /**
   * Now I can modify the radius of this BrandIdentity's Circle Icon by doing the following
   * And this modification only occur on a BrandIdentity only if it's Shape of the Icon is a Circle
   * Otherwise it will leave it intact
   */
  val enlargedRadius = brandCircleR.modify(_ + 10)(aBrand)
  println(enlargedRadius)

  val aTriangleBrand = BrandIdentity(Logo("Yellow"), Icon("Black", Triangle(54, 54, 54)))
  val enlargeTriangle = brandCircleR.modify(_ + 10)(aTriangleBrand) //Leaving it intact
  println(enlargeTriangle)

}
