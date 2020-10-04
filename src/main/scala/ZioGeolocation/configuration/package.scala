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

import pureconfig.generic.auto._
import pureconfig.ConfigSource
import zio.{ Has, Layer, Task, URIO, ZIO, ZLayer }

package object configuration {

  type Configuration = Has[AppConfig] with Has[GeocodingConfig]

  final case class Config(
    app: AppConfig,
    geocoding: GeocodingConfig
  )

  final case class AppConfig(
    port: Int,
    endpoint: String
  )

  final case class GeocodingConfig(
    apikey: String
  )

  val appConfig: URIO[Has[AppConfig], AppConfig]                   = ZIO.access(_.get)
  val geocodingConfig: URIO[Has[GeocodingConfig], GeocodingConfig] = ZIO.access(_.get)

  object Configuration {

    val live: Layer[Throwable, Configuration] = ZLayer.fromEffectMany(
      Task
        .effect(ConfigSource.default.loadOrThrow[Config])
        .map(c => Has(c.app) ++ Has(c.geocoding))
    )
  }
}
