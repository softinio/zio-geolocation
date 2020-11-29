import mill._, scalalib._, scalafmt._

object ZioGeolocation extends ScalaModule with ScalafmtModule {
  val zioVersion        = "1.0.1"
  val zioCatsVersion    = "2.1.4.0"
  val sttpVersion       = "1.7.2"
  val scalaIsoVersion   = "0.1.2"
  val circeVersion      = "0.12.3"
  val pureconfigVersion = "0.14.0"
  val http4sVersion     = "0.21.7"
  val silencerVersion   = "1.4.1"

  def scalaVersion = "2.12.12"

  def scalacOptions =
        Seq(
          "-opt-warnings",
          "-Ywarn-extra-implicit",
          "-Ywarn-unused:_,imports",
          "-Ywarn-unused:imports",
          "-opt:l:inline",
          "-opt-inline-from:<source>",
          "-Ypartial-unification",
          "-deprecation"
        )

  def ivyDeps = Agg(
    ivy"dev.zio::zio:${zioVersion}",
    ivy"dev.zio::zio-interop-cats:${zioCatsVersion}",
    ivy"com.vitorsvieira::scala-iso:${scalaIsoVersion}",
    ivy"com.softwaremill.sttp::core:${sttpVersion}",
    ivy"com.softwaremill.sttp::async-http-client-backend-zio:${sttpVersion}",
    ivy"com.softwaremill.sttp::circe:${sttpVersion}",
    ivy"org.http4s::http4s-dsl:${http4sVersion}",
    ivy"org.http4s::http4s-blaze-server:${http4sVersion}",
    ivy"org.http4s::http4s-blaze-client:${http4sVersion}",
    ivy"org.http4s::http4s-circe:${http4sVersion}",
    ivy"io.circe::circe-core:${circeVersion}",
    ivy"io.circe::circe-generic:${circeVersion}",
    ivy"io.circe::circe-parser:${circeVersion}",
    ivy"com.github.pureconfig::pureconfig:${pureconfigVersion}"
  )

  def compileIvyDeps = Agg(
    ivy"com.github.ghik::silencer-lib:${silencerVersion}"
  )

  def scalacPluginIvyDeps = Agg(
    ivy"com.github.ghik::silencer-plugin:${silencerVersion}"
  )

  object test extends Tests {
    def compileIvyDeps = Agg(
      ivy"com.github.ghik::silencer-lib:${silencerVersion}"
    )

    def scalacPluginIvyDeps = Agg(
      ivy"com.github.ghik::silencer-plugin:${silencerVersion}"
    )

    def ivyDeps = Agg(
      ivy"dev.zio::zio-test:${zioVersion}",
      ivy"dev.zio::zio-test-sbt:${zioVersion}"
    )
    def testFrameworks = Seq("zio.test.sbt.ZTestFramework")
  }
}
