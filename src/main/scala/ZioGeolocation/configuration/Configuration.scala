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
