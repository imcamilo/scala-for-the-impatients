scalaVersion := "2.13.6"

name := "scala-review"
organization := "com.github.imcamilo"
version := "1.0"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.0.0",
  "io.circe" %% "circe-core" % "0.14.1",
  "io.circe" %% "circe-generic" % "0.14.1",
  "io.circe" %% "circe-parser" % "0.14.1",
  "commons-io" % "commons-io" % "2.11.0",
  "dev.optics" %% "monocle-core"  % "3.1.0",
  "dev.optics" %% "monocle-macro" % "3.1.0",
  "dev.zio" %% "zio" % "1.0.4-2",
  "dev.zio" %% "zio-streams" % "1.0.4-2",
)

Global / scalacOptions += "-Ymacro-annotations"