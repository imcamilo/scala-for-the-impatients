scalaVersion := "2.13.10"

name := "scala-review"
organization := "com.github.imcamilo"
version := "1.0"

val Http4sVersion = "1.0.0-M21"
val CirceVersion = "0.14.5"
val CommonsIOVersion = "2.11.0"
val MonocleVersion = "3.1.0"
val ZIOVersion = "1.0.4-2"
val FinagleVersion = "22.12.0"
val CatsVersion = "2.9.0"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % CatsVersion,
  "org.http4s" %% "http4s-ember-server" % Http4sVersion,
  "org.http4s" %% "http4s-ember-client" % Http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s" %% "http4s-circe" % Http4sVersion,
  "org.http4s" %% "http4s-dsl" % Http4sVersion,
  "io.circe" %% "circe-generic" % CirceVersion,
  "io.circe" %% "circe-core" % CirceVersion,
  "io.circe" %% "circe-parser" % CirceVersion,
  "commons-io" % "commons-io" % CommonsIOVersion,
  "dev.optics" %% "monocle-core" % MonocleVersion,
  "dev.optics" %% "monocle-macro" % MonocleVersion,
  "dev.zio" %% "zio" % ZIOVersion,
  "dev.zio" %% "zio-streams" % ZIOVersion,
  "org.scala-lang" % "scala-reflect" % "2.13.10",
  "com.twitter" %% "finagle-http" % FinagleVersion,
  "co.fs2" %% "fs2-core" % "3.7.0", // available for 2.12, 2.13, 3.2
  "co.fs2" %% "fs2-io" % "3.7.0", // optional I/O library
  "co.fs2" %% "fs2-reactive-streams" % "3.7.0", // optional reactive streams interop
  "co.fs2" %% "fs2-scodec" % "3.7.0" // optional scodec interop
)

Global / scalacOptions += "-Ymacro-annotations"
