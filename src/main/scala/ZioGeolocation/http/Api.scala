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

import io.circe.generic.auto._
import io.circe.{ Decoder, Encoder }
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{ EntityDecoder, EntityEncoder, HttpRoutes }
import zio._
import zio.interop.catz._

import ZioGeolocation.configuration.Configuration
import ZioGeolocation.geocoding.Geocoding

final case class Api[R <: Geocoding](rootUri: String) {
  println(rootUri)

  type GeoIO[A] = RIO[R, A]

  implicit def circeJsonDecoder[A](implicit decoder: Decoder[A]): EntityDecoder[GeoIO, A] = jsonOf[GeoIO, A]
  implicit def circeJsonEncoder[A](implicit decoder: Encoder[A]): EntityEncoder[GeoIO, A] = jsonEncoderOf[GeoIO, A]

  val dsl: Http4sDsl[GeoIO] = Http4sDsl[GeoIO]
  import dsl._

  def route: HttpRoutes[GeoIO] =
    HttpRoutes.of[GeoIO] {
      case GET -> Root / "ping" => Ok("pong")
      case request @ POST -> Root =>
        request.decode[Request] { req =>
          for {
            cfg <- configuration.geocodingConfig.provideSomeLayer(Configuration.live)
            result <- geocoding.getGeo(
                       address = req.address,
                       postalCode = req.postalCode,
                       countryCode = req.countryCode,
                       settings = GeocodingSettings(apiKey = cfg.apikey)
                     )
            response <- Created(result)

          } yield response
        }
    }
}
