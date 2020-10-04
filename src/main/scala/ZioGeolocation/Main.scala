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

import cats.effect.{ ExitCode => CatsExitCode }
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.CORS
import zio._
import zio.blocking.Blocking
import zio.clock.Clock
import zio.console._
import zio.interop.catz._

import ZioGeolocation.configuration.Configuration
import ZioGeolocation.geocoding.Geocoding

object Main extends App {

  type AppEnvironment = Configuration with Clock with Geocoding

  type AppTask[A] = RIO[AppEnvironment, A]

  val appLayers = (Configuration.live ++ Blocking.live) ++ Geocoding.live

  override def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] = {
    val program: ZIO[AppEnvironment, Throwable, Unit] =
      for {
        api    <- configuration.appConfig
        httpApp = Router[AppTask](
                    "/" -> Api(s"${api.endpoint}/").route
                  ).orNotFound

        server <- ZIO
                    .runtime[AppEnvironment]
                    .flatMap { implicit rts =>
                      BlazeServerBuilder[AppTask]
                        .bindHttp(api.port, api.endpoint)
                        .withHttpApp(CORS(httpApp))
                        .serve
                        .compile[AppTask, AppTask, CatsExitCode]
                        .drain
                    }
      } yield server

    program
      .provideSomeLayer[ZEnv](appLayers)
      .tapError(err => putStrLn(s"Execution failed with: $err"))
      .exitCode
  }
}
