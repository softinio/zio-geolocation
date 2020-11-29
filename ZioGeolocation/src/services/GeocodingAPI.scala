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
import com.softwaremill.sttp._
import com.softwaremill.sttp.circe._
import com.vitorsvieira.iso._
import zio._

/**
 * Service calling Google's Geocoding API
 * https://developers.google.com/maps/documentation/geocoding/start
 */
object GeocodingAPI {
  implicit val backend   = HttpURLConnectionBackend()
  val geocodeUrl: String = "https://maps.googleapis.com/maps/api/geocode/json"

  def getLocation(requestDetails: GeoRequest): Task[GeoResponse] = {
    val validCountryCode: Option[String] = requestDetails.countryComponent.map(ISOCountry(_).toString)
    val url                              =
      uri"$geocodeUrl?address=$requestDetails.address&key=$requestDetails.key&postal_code=$requestDetails.postalCodeComponent&country=$validCountryCode"
    val response                         = sttp
      .get(url)
      .response(asJson[GeoResponse])
      .send()

    response.body match {
      case Left(error)     => ZIO.fail(new Exception(error))
      case Right(received) =>
        received match {
          case Left(jsonError) => ZIO.fail(new Exception(jsonError.message))
          case Right(parsed)   =>
            if (parsed.results.isEmpty) ZIO.fail(new Exception(parsed.status))
            else ZIO.succeed(parsed)
        }
    }
  }
}
