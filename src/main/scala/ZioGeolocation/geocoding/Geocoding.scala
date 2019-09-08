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

import zio._

trait Geocoding extends Serializable {
  val userGeocoding: Geocoding.Service[Any]
}

object Geocoding {
  trait Service[R] extends Serializable {
    def get(
      address: String,
      postalCode: Option[String],
      countryCode: Option[String],
      settings: GeocodingSettings
    ): TaskR[R, List[Response]]
  }
}

trait GeoLive extends Geocoding {
  override val userGeocoding: Geocoding.Service[Any] = new Geocoding.Service[Any] {
    private def createResponse(location: Results): Response =
      Response(
        address = location.formattedAddress,
        quality = location.geometry.locationType
      )

    def get(
      address: String,
      postalCode: Option[String],
      countryCode: Option[String],
      settings: GeocodingSettings
    ): Task[List[Response]] = {
      val request: GeoRequest = GeoRequest(
        address = address,
        key = settings.apiKey,
        postalCodeComponent = postalCode,
        countryComponent = countryCode
      )
      for {
        locations <- GeocodingAPI.getLocation(request)
      } yield (locations.results.map(location => createResponse(location)))
    }
  }
}

object GeocodingLive extends GeoLive
