package com.github.imcamilo.typelevel
/*
object Http4sStarter extends IOApp {

  type Actor = String
  case class Movie(id: String, title: String, year: Int, actor: List[Actor], director: Actor)
  case class Director(firstName: String, lastName: String) {
    override def toString: Actor = s"$firstName $lastName"
  }
  case class DirectorDetails(firstName: String, lastName: String, genre: String)

  // internal db
  val snjl: Movie = Movie(
    "6bcbca1e-efd3-411d-9f7c-14b872444fce",
    "Zack Snyder's Justice League",
    2021,
    List("Henry Cavill", "Gal Godot", "Ezra Miller", "Ben Affleck", "Ray Fisher", "Jason Momoa"),
    "Zack Snyder"
  )
  val movies: Map[String, Movie] = Map(snjl.id -> snjl)
  // business logic
  private def findMovieById(movieId: UUID): Option[Movie] = movies.get(movieId.toString)
  private def findMoviesByDirector(director: String): List[Movie] = movies.values.filter(_.director == director).toList

  /** GET Endpoints
 *    - get all movies for a director under a given year
 *    - all actors for a movie
 *    - details about a director
 *
 *  POST Endpoints
 *    - add a new director
 */

  // http4s is built on the concept of request and response
  // Http Server:
  // Request -> F[Option[Response]] = HttpRoute[F]

  // query parameters matchers, param extractors
  object DirectorQueryParamMatcher
      extends QueryParamDecoderMatcher[String]("director") // decoders for string already exists

  // implicit missing or given instance (3)
  implicit val yearQueryParamEncoder: QueryParamDecoder[Year] = QueryParamDecoder[Int].emap(yearInt => {
    Try(Year.of(yearInt)).toEither
      .leftMap(e => ParseFailure(e.getMessage, e.getMessage))
  })
  object YearQueryParamMatcher extends OptionalValidatingQueryParamDecoderMatcher[Year]("year")

  // imports ...
  // Get /api/movies?director=Zack%20Snyder&year=2021

  // no implicit found for Monad[F]
  def moviesRoutes[F[_]: Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F] // apply method that retunrs a bunch of useful methods
    HttpRoutes.of[F] {
      case GET -> Root / "movies" :? DirectorQueryParamMatcher(director) +& YearQueryParamMatcher(maybeYear) =>
        val moviesByDirector = findMoviesByDirector(director)
        maybeYear match { // fold for getting response
          case Some(validatedYear) =>
            validatedYear.fold(
              _ => BadRequest("The year was badly formatted"),
              year => {
                val moviesByDirectorAndYear = moviesByDirector.filter(_.year == year.getValue)
                Ok(moviesByDirectorAndYear.asJson)
              }
            )
          case None => Ok(moviesByDirector.asJson)
        }
      case GET -> Root / "movies" / UUIDVar(movieId) / "actors" =>
        findMovieById(movieId).map(_.actor) match {
          case Some(actors) => Ok(actors.asJson)
          case _ => NotFound(s"No movie with movie id $movieId found in database")
        }
    }
  }

  object DirectorPath {
    def unapply(string: String): Option[Director] = {
      Try {
        val tokens = string.split(" ")
        Director(tokens(0), tokens(1))
      }.toOption
    }
  }

  val directorDetailsDB: mutable.Map[Director, DirectorDetails] =
    mutable.Map(Director("Zack", "Snyder") -> DirectorDetails("Zack", "Snyder", "superhero"))

  def directorRoutes[F[_]: Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F] // apply method that returns a bunch of useful methods
    HttpRoutes.of[F] { case GET -> Root / "directors" / DirectorPath(director) =>
      directorDetailsDB.get(director) match {
        case Some(dirDetails) => Ok(dirDetails.asJson)
        case _ => NotFound(s"No director $director found")
      }
    }
  }

  // all routes

  def allRoutes[F[_]: Monad]: HttpRoutes[F] = {
    moviesRoutes[F] <+> directorRoutes[F] // cats.syntax.semigroupk._ comes in cats.implicit._
  }

  def allRoutesComplete[F[_]: Monad]: HttpApp[F] = allRoutes[F].orNotFound


  // override run
  override def run(args: List[String]): IO[ExitCode] = {
    val apis = Router(
      "/api"-> moviesRoutes[IO],
      "/api/admin" -> directorRoutes[IO]
    ).orNotFound

    BlazeServerBuilder[IO](runtime.compute)
      .bindHttp(8766, "localhost")
      .withHttpApp(allRoutesComplete) // alternative apis
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)

  }

}
 */
