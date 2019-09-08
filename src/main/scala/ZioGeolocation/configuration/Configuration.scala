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

import pureconfig.loadConfigOrThrow
import zio.{ Task, TaskR }
import pureconfig.generic.auto._

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

trait Configuration extends Serializable {
  val config: Configuration.Service[Any]
}

object Configuration {
  trait Service[R] {
    val load: TaskR[R, Config]
  }
}

trait ConfigLive extends Configuration {
  override val config: Configuration.Service[Any] = new Configuration.Service[Any] {
    val load: Task[Config] = Task.effect(loadConfigOrThrow[Config])
  }
}

object ConfigurationLive extends ConfigLive
