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

/**
 * Algrebra Data Types descibing the response received from Geocode API
 */
final case class AddressComponent(longName: String, shortName: String, types: List[String])

final case class AddressComponents(component: List[AddressComponent])

final case class Location(lat: BigDecimal, lng: BigDecimal)

final case class NorthEast(lat: BigDecimal, lng: BigDecimal)
final case class SouthWest(lat: BigDecimal, lng: BigDecimal)
final case class Viewport(northeast: NorthEast, southwest: SouthWest)

final case class Geometry(location: Location, locationType: String, viewport: Viewport)

final case class Results(
  addressComponents: AddressComponents,
  formattedAddress: String,
  geometry: Geometry,
  placeId: String,
  types: List[String]
)

final case class GeoResponse(results: List[Results], status: String)
