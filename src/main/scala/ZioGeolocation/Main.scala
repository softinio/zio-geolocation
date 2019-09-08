/*
 * Copyright 2019 Salar Rahmanian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

  type AppEnvironment = Console with Clock with Geocoding with Configuration

  type AppTask[A] = TaskR[AppEnvironment, A]

  override def run(args: List[String]): ZIO[Environment, Nothing, Int] = {
    val program: ZIO[Main.Environment, Throwable, Unit] = for {
      conf       <- configuration.load.provide(ConfigurationLive)
      blockingEC <- blocking.blockingExecutor.map(_.asEC).provide(Blocking.Live)

      httpApp = Router[AppTask](
        "/" -> Api(s"${conf.app.endpoint}/").route
      ).orNotFound

      server <- ZIO
                 .runtime[AppEnvironment]
                 .flatMap { implicit rts =>
                   BlazeServerBuilder[AppTask]
                     .bindHttp(conf.app.port, "0.0.0.0")
                     .withHttpApp(CORS(httpApp))
                     .serve
                     .compile[AppTask, AppTask, ExitCode]
                     .drain
                 }
                 .provideSome[Environment] { base =>
                   new Console with Clock with Geocoding with Configuration {
                     override val console: Console.Service[Any]         = base.console
                     override val clock: Clock.Service[Any]             = base.clock
                     override val config: Configuration.Service[Any]    = ConfigurationLive.config
                     override val userGeocoding: Geocoding.Service[Any] = GeocodingLive.userGeocoding
                   }
                 }
    } yield server

    program.foldM(
      err => putStrLn(s"Execution failed with: $err") *> IO.succeed(1),
      _ => IO.succeed(0)
    )
  }
}
