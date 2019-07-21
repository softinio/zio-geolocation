package ZioGeolocation

import cats.effect.ExitCode
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.CORS
import zio._
import zio.blocking.Blocking
import zio.clock.Clock
import zio.console._
import zio.interop.catz._

object Main extends App {

  type AppEnvironment = Clock with Geocoding

  type AppTask[A] = TaskR[AppEnvironment, A]

  override def run(args: List[String]): ZIO[Environment, Nothing, Int] = {
    val program: ZIO[Main.Environment, Throwable, Unit] = for {
      // conf        <- configuration.load.provide(Configuration.Live)
      blockingEC <- blocking.blockingExecutor.map(_.asEC).provide(Blocking.Live)

      httpApp = Router[AppTask](
        // "/geocode" -> Api(s"${conf.api.endpoint}/geocode").route
        "/geocode" -> Api("http://localhost/geocode").route
      ).orNotFound

      server = ZIO.runtime[AppEnvironment].flatMap { implicit rts =>
        BlazeServerBuilder[AppTask]
        // .bindHttp(conf.api.port, "0.0.0.0")
          .bindHttp(8000, "0.0.0.0")
          .withHttpApp(CORS(httpApp))
          .serve
          .compile[AppTask, AppTask, ExitCode]
          .drain
      }
    } yield server

    program.foldM(
      err => putStrLn(s"Execution failed with: $err") *> IO.succeed(1),
      _ => IO.succeed(0)
    )
  }
}
