package com.github.imcamilo.review.adt

object AlgebraicDataTypes extends App {

  //way of structuring your data

  //no other scala file can extend this

  sealed trait Weather //"Sum type"
  case object Sunny extends Weather
  case object Windy extends Weather
  case object Rainy extends Weather
  case object Cloudy extends Weather

  // Weather = Sunny + Windy + Rainy + Cloudy == Sum Type
  // usually implemented as a sealed trait, or sealed abstract class

  def feeling(weather: Weather): String = weather match {
    case Sunny => ":)"
    case Windy => ":("
    case Rainy => ":D"
    case Cloudy => ":|"
  }

  case class WeatherForecastRequest(latitude: Double, longitude: Double)
  // (Double, Double) => WFR
  // type WFR = Double x Double (cartesian product) = Product Type

  // hybrid types
  // SUM type
  sealed trait WeatherForecastResponse
  // but each of the possible cases is the PRODUCT type
  case class Valid(weather: Weather) extends WeatherForecastResponse
  case class Invalid(error: String, description: String) extends WeatherForecastResponse

  //advantages
  // 1. ilegal states are NOT representables
  // 2. highly composable
  // 3. inmutable data structures
  // 4. they contains just data, not funcionality => structure our code

  //in this case, it is not limiting wrong types
  type NaiveWeather = String
  def naiveFeeling(weather: String) = weather match {
    case "sunny" => ":)"
  }
  naiveFeeling("45 degrees")

  //complexity = Number of possible values of that ADT
  // goal : Reduce complexity

  sealed trait WeatherServerError
  case object NotAvailable extends WeatherServerError
  // for replacing string errors on Invalid.
  // other classes

}
