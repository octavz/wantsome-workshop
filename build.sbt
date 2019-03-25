name := "lists-demo"
organization := "org.wantsome"
version := "0.0.1-SNAPSHOT"
scalaVersion := "2.12.7"

val Http4sVersion = "0.20.0-M1"
val circeV = "0.11.1"
val catsEffectV = "1.0.0"
val catsVersion = "1.5.0"
val log4catsVersion = "0.2.0"
val doobieVersion = "0.6.0"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

scalacOptions ++= Seq("-Ypartial-unification", "-language:higherKinds")

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s" %% "http4s-circe" % Http4sVersion,
  "org.http4s" %% "http4s-dsl" % Http4sVersion,
  "io.circe" %% "circe-generic" % "0.10.0-M1",
  "io.circe" %% "circe-literal" % "0.10.0-M1",
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "org.tpolecat" %% "doobie-h2" % doobieVersion,
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-effect" % "1.1.0",
  "org.typelevel" %% "cats-mtl-core" % "0.4.0",
  "io.chrisdavenport" %% "log4cats-slf4j" % log4catsVersion,
  "com.github.mpilquist" %% "simulacrum" % "0.14.0",
  "com.olegpy" %% "meow-mtl" % "0.2.0",
  "com.github.pureconfig" %% "pureconfig" % "0.10.1",
  "com.github.mpilquist" %% "simulacrum" % "0.15.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.h2database" % "h2" % "1.4.199" % Compile,
  "org.fusesource.jansi" % "jansi" % "1.8" % Runtime,
  "org.scalacheck" %% "scalacheck" % "1.14.0" % Test,
  "org.scalatest" %% "scalatest" % "3.0.5" % Test)

mainClass in reStart := Some("org.wantsome.backend.Server")

