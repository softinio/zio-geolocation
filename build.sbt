import Build._

ThisBuild / scalaVersion := "2.12.8"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.softinio"
ThisBuild / organizationName := "softinio.com"

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")

val zioVersion      = "1.0.0-RC9"
val sttpVersion     = "1.6.0"
val scalaIsoVersion = "0.1.2"
val json4sVersion    = "3.6.0"
val specs2Version   = "4.6.0"

libraryDependencies ++= Seq(
  "dev.zio"               %% "zio"                           % zioVersion,
  "com.vitorsvieira"      %% "scala-iso"                     % scalaIsoVersion,
  "com.softwaremill.sttp" %% "core"                          % sttpVersion,
  "com.softwaremill.sttp" %% "async-http-client-backend-zio" % sttpVersion,
  "com.softwaremill.sttp" %% "json4s"                        % sttpVersion,
  "org.json4s"            %% "json4s-native"                 % json4sVersion,
  "org.specs2"            %% "specs2-core"                   % specs2Version % "test",
  "org.specs2"            %% "specs2-scalacheck"             % specs2Version % Test,
  "org.specs2"            %% "specs2-matcher-extra"          % specs2Version % Test
)

lazy val root =
  (project in file("."))
    .settings(
      stdSettings("zio-geolocation")
    )