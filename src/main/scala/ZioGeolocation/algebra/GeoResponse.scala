package ZioGeolocation

/**
 *
 * Algrebra Data Types descibing the response received from Geocode API
 *
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
