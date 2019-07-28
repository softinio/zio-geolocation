package ZioGeolocation

import io.circe.generic.auto._
import io.circe.{ Decoder, Encoder }
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{ EntityDecoder, EntityEncoder, HttpRoutes }
import zio._
import zio.interop.catz._

final case class Api[R <: Geocoding](rootUri: String) {
  type GeoIO[A] = TaskR[R, A]

  implicit def circeJsonDecoder[A](implicit decoder: Decoder[A]): EntityDecoder[GeoIO, A] = jsonOf[GeoIO, A]
  implicit def circeJsonEncoder[A](implicit decoder: Encoder[A]): EntityEncoder[GeoIO, A] =
    jsonEncoderOf[GeoIO, A]

  val dsl: Http4sDsl[GeoIO] = Http4sDsl[GeoIO]
  import dsl._

  def route: HttpRoutes[GeoIO] =
    HttpRoutes.of[GeoIO] {
      case request @ POST -> Root =>
        request.decode[Request] { req => 
          for {
            cfg <- configuration.load.provide(Configuration.Live)
            result <- geocoding.get(
                address = req.address,
                postalCode = req.postalCode,
                countryCode = req.countryCode,
                settings = GeocodingSettings(apiKey = cfg.geocoding.apikey))
            response <- Created(result)

          } yield response
      }
  }
}
