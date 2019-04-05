
val Http4sVersion = "0.20.0-M1"
val circeV = "0.11.1"
val catsEffectV = "1.0.0"
val catsVersion = "1.5.0"
val log4catsVersion = "0.2.0"
val doobieVersion = "0.6.0"

lazy val root = project.in(file("."))
  .configs(IntegrationTest)
  .settings(
    name := "lists-demo",
    organization := "org.wantsome",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.8",
    scalacOptions ++= Seq("-Ypartial-unification", "-language:higherKinds"),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full),
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.8"),
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
      "io.estatico" %% "newtype" % "0.4.2",
      "org.fusesource.jansi" % "jansi" % "1.8" % Runtime,
      "com.chuusai" %% "shapeless" % "2.3.3",
      "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % "1.2.0",
      "org.scalacheck" %% "scalacheck" % "1.14.0",
      "org.scalatest" %% "scalatest" % "3.0.5"),
    mainClass in reStart := Some("org.wantsome.backend.Server"),
    initialCommands in console := "import org.wantsome.backend.actions._",
    Defaults.itSettings)

