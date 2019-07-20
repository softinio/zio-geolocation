package ZioGeolocation

import zio._
import zio.blocking.Blocking
import zio.clock.Clock
import zio.console._

object Main extends App {
  override def run(args: List[String]): ZIO[Environment, Nothing, Int] =
    (for {
      blockingEC <- ZIO.environment[Blocking].flatMap(_.blocking.blockingExecutor).map(_.asEC)
    } yield 0).foldM(e => UIO(println(e.toString())).const(1), IO.succeed)
}
