
import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

val http4sVersion = "0.20.0-M1"
val circeVersion = "0.11.1"
val catsEffectVersion = "1.2.0"
val catsVersion = "1.6.0"
val log4catsVersion = "0.2.0"
val doobieVersion = "0.6.0"
val catsMtlVersion = "0.4.0"

lazy val shared = Seq(
  organization := "org.wantsome",
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.12.8",
  fork in Test := false,
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full),
  addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.8"),
  scalacOptions ++= Seq(
    "-Ypartial-unification",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked"),
  libraryDependencies ++= Seq(
    "org.scalatest" %%% "scalatest" % "3.0.5" % Test,
    "com.github.alexarchambault" %%% "scalacheck-shapeless_1.14" % "1.2.0" % Test,
    "org.typelevel" %%% "cats-core" % catsVersion,
    "org.typelevel" %%% "cats-effect" % catsEffectVersion,
    "org.typelevel" %%% "cats-mtl-core" % catsMtlVersion,
    "io.circe" %%% "circe-generic" % circeVersion,
    "io.circe" %%% "circe-literal" % circeVersion,
    "io.estatico" %%% "newtype" % "0.4.2",
    "com.chuusai" %%% "shapeless" % "2.3.3",
    "org.typelevel" %%% "cats-mtl-core" % "0.4.0",
    "com.olegpy" %%% "meow-mtl" % "0.2.0",
    "com.github.mpilquist" %%% "simulacrum" % "0.15.0"
  )
)

lazy val common = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("common"))
  .settings(shared: _*)

lazy val backend = project.in(file("backend"))
  .configs(IntegrationTest)
  .settings(shared: _*)
  .settings(name := "lists-backend",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-h2" % doobieVersion,
      "io.chrisdavenport" %% "log4cats-slf4j" % log4catsVersion,
      "com.github.pureconfig" %% "pureconfig" % "0.10.1",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.h2database" % "h2" % "1.4.199" % Compile,
      "org.fusesource.jansi" % "jansi" % "1.8" % Runtime
    ),
    mainClass in reStart := Some("org.wantsome.backend.Main"),
    initialCommands in console := "import org.wantsome.backend.util._",
    Defaults.itSettings
  ).dependsOn(common.jvm)


lazy val frontend = project.in(file("frontend"))
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(shared: _*)
  .settings(
    name := "lists-frontend",
    libraryDependencies ++= Seq(
      "me.shadaj" %%% "slinky-web" % "0.6.0" // React DOM, HTML and SVG tags
      , "me.shadaj" %%% "slinky-hot" % "0.6.0" // Hot loading, requires react-proxy package
      , "com.olegpy" %%% "shironeko-core" % "0.0.9"
      , "com.olegpy" %%% "shironeko-slinky" % "0.0.9"
    ),
    npmDependencies in Compile ++= Seq(
      "react" -> "16.8.6"
      , "react-dom" -> "16.8.6"
      , "react-proxy" -> "1.1.8"
      , "file-loader" -> "3.0.1"
      , "style-loader" -> "0.23.1"
      , "css-loader" -> "2.1.1"
      , "html-webpack-plugin" -> "3.2.0"
      , "copy-webpack-plugin" -> "5.0.2"
      , "webpack-merge" -> "4.2.1")
    , scalacOptions += "-P:scalajs:sjsDefinedByDefault"
    , version in webpack := "4.29.6"
    , version in startWebpackDevServer := "3.2.1"
    , webpackResources := baseDirectory.value / "webpack" * "*"
    , webpackConfigFile in fastOptJS := Some(baseDirectory.value / "webpack" / "webpack-fastopt.config.js")
    , webpackConfigFile in fullOptJS := Some(baseDirectory.value / "webpack" / "webpack-opt.config.js")
    , webpackConfigFile in Test := Some(baseDirectory.value / "webpack" / "webpack-core.config.js")
    , webpackDevServerExtraArgs in fastOptJS := Seq("--inline", "--hot")
    , webpackBundlingMode in fastOptJS := BundlingMode.LibraryOnly()
    , requireJsDomEnv in Test := true
    , addCommandAlias("dev", ";fastOptJS::startWebpackDevServer;~fastOptJS")
    , addCommandAlias("build", "fullOptJS::webpack")
  ).dependsOn(common.js)

