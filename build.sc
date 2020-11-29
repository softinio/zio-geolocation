import mill._, scalalib._, scalafmt._

object ZioGeolocation extends ScalaModule with ScalafmtModule {
  def scalaVersion = "2.12.12"

  def scalacOptions =
        Seq(
          "-opt-warnings",
          "-Ywarn-extra-implicit",
          "-Ywarn-unused:_,imports",
          "-Ywarn-unused:imports",
          "-opt:l:inline",
          "-opt-inline-from:<source>",
          "-Ypartial-unification"
        )

  def ivyDeps = Agg(
    ivy"dev.zio::zio:1.0.1",
    ivy"dev.zio::zio-interop-cats:2.1.4.0",
    ivy"com.vitorsvieira::scala-iso:0.1.2",
    ivy"com.softwaremill.sttp::core:1.7.2",
    ivy"com.softwaremill.sttp::async-http-client-backend-zio:1.7.2",
    ivy"com.softwaremill.sttp::circe:1.7.2",
    ivy"org.http4s::http4s-dsl:0.21.7",
    ivy"org.http4s::http4s-blaze-server:0.21.7",
    ivy"org.http4s::http4s-blaze-client:0.21.7",
    ivy"org.http4s::http4s-circe:0.21.7",
    ivy"io.circe::circe-core:0.12.3",
    ivy"io.circe::circe-generic:0.12.3",
    ivy"io.circe::circe-parser:0.12.3",
    ivy"com.github.pureconfig::pureconfig:0.14.0"
  )

  def compileIvyDeps = Agg(
    ivy"com.github.ghik::silencer-lib:1.4.1"
  )

  def scalacPluginIvyDeps = Agg(
    ivy"com.github.ghik::silencer-plugin:1.4.1"
  )

  object test extends Tests {
    def ivyDeps = Agg(
      ivy"dev.zio::zio-test:1.0.1",
      ivy"dev.zio::zio-test-sbt:1.0.1"
    )
    def testFrameworks = Seq("zio.test.sbt.ZTestFramework")
  }
}
