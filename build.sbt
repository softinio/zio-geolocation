import Build._

ThisBuild / scalaVersion := "2.12.12"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.softinio"
ThisBuild / organizationName := "softinio.com"
ThisBuild / turbo := false

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")

val zioVersion        = "1.0.1"
val zioCatsVersion    = "2.1.4.0"
val sttpVersion       = "1.7.2"
val scalaIsoVersion   = "0.1.2"
val circeVersion      = "0.12.3"
val pureconfigVersion = "0.14.0"
val specs2Version     = "4.10.0"
val http4sVersion     = "0.21.7"

libraryDependencies ++= Seq(
  "dev.zio"               %% "zio"                           % zioVersion,
  "dev.zio"               %% "zio-interop-cats"              % zioCatsVersion,
  "com.vitorsvieira"      %% "scala-iso"                     % scalaIsoVersion,
  "com.softwaremill.sttp" %% "core"                          % sttpVersion,
  "com.softwaremill.sttp" %% "async-http-client-backend-zio" % sttpVersion,
  "com.softwaremill.sttp" %% "circe"                         % sttpVersion,
  "org.http4s"            %% "http4s-dsl"                    % http4sVersion,
  "org.http4s"            %% "http4s-blaze-server"           % http4sVersion,
  "org.http4s"            %% "http4s-blaze-client"           % http4sVersion,
  "org.http4s"            %% "http4s-circe"                  % http4sVersion,
  "io.circe"              %% "circe-core"                    % circeVersion,
  "io.circe"              %% "circe-generic"                 % circeVersion,
  "io.circe"              %% "circe-parser"                  % circeVersion,
  "com.github.pureconfig" %% "pureconfig"                    % pureconfigVersion,
  "org.specs2"            %% "specs2-core"                   % specs2Version % "test",
  "org.specs2"            %% "specs2-scalacheck"             % specs2Version % Test,
  "org.specs2"            %% "specs2-matcher-extra"          % specs2Version % Test
)

lazy val root =
  (project in file("."))
    .settings(
      stdSettings("zio-geolocation")
    )
