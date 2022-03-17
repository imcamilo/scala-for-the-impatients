package com.github.imcamilo.monocle

/*
Monocle was invented because nested data structures are a pain to inspect and change.
The pain increases with the depth of the data structures
 */
object Lenses extends App {

  case class Guitar(make: String, model: String)
  case class Guitarist(name: String, favoriteGuitar: Guitar)
  case class RockBand(name: String, yearFormed: Int, leadGuitarist: Guitarist)

  val metallica = RockBand("Metallica", 1981, Guitarist("Kirk Hammett", Guitar("ESP", "M II")))
  println(metallica)

  val metallicaFixed = metallica.copy(
    leadGuitarist = metallica.leadGuitarist.copy(
      favoriteGuitar = metallica.leadGuitarist.favoriteGuitar.copy(
        model = metallica.leadGuitarist.favoriteGuitar.model.replace(" ", "-")
      )
    )
  )
  println(metallicaFixed)

  /**
   * Monocle give us the capability to access a deeply nested field in a data structure, inspected or change it
   * at will therefore creating a new data structure as a result.
   */

  val kirksFavGuitar = Guitar("ESP", "M II")

  import monocle.Lens
  import monocle.macros.GenLens

  /**
   * Lens receive 2 types
   * The initial data structure that I'm inspecting -Guitar-
   * The type of the field I want to inspecting -String-
   * Then you will need to access the field of a Guitar which is of type String (model)
   * GenLens is based on Scala Macro there are a lot of magic behind the scenes
   *
   * I can use this lens to access the field inside the guitar instance.
   * If I want to inspect a field I would use this Lens to Zoom into the data structure
   */
  val guitarModelLens: Lens[Guitar, String] = GenLens[Guitar](_.model)
  //INSPECT A FIELD
  //I need to pass in the initial guitar and by calling .get I would inspect this data structure
  //and I would return whatever the lens is designed to obtain (in this case the model of the guitar)
  val kirksGuitarModel = guitarModelLens.get(kirksFavGuitar)
  println(kirksGuitarModel)

  //MODIFY A FIELD
  //this will return another guitar whose field described by this lends
  val formattedGuitar = guitarModelLens.modify(_.replace(" ", "-"))(kirksFavGuitar)
  println(formattedGuitar)

  //A lens has is able to zoom into the field of a data structure
  //The power of this lenses will bacome appear when we compose them

  //COMPOSING LENSES
  //takes a RockBand and returns a Guitarist
  val leaderGuitaristLens = GenLens[RockBand](_.leadGuitarist)
  //takes a Guitarist and returns a Guitar
  val favoriteGuitarLens = GenLens[Guitarist](_.favoriteGuitar)
  //This lens is able to zoom into a RockBand
  //is able to zoom very deep into a RockBand and extract or inspect the favorite guitar model
  val composedLens = leaderGuitaristLens.andThen(favoriteGuitarLens).andThen(guitarModelLens)

  //With this lens I can follow the same pattern that I followed above in the simple case
  val kirksGuitarModel2 = composedLens.get(metallica)
  println(kirksGuitarModel2)
  val metallicaFixed2 = composedLens.modify(_.replace("M", "Z"))(metallica)
  println(metallicaFixed2.leadGuitarist.favoriteGuitar.model)

  //Lenses are reusable

}
