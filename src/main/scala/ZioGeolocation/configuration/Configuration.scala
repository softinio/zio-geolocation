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

  trait Live extends Configuration {
    val config: Service[Any] = new Service[Any] {
      val load: Task[Config] = Task.effect(loadConfigOrThrow[Config])
    }
  }

  object Live extends Live
}
