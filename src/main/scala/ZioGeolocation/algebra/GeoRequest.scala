package ZioGeolocation

/**
 *
 * Algrebra Data Types descibing the request sent to Geocode API
 *
 */
final case class GeoRequest(
  address: String,
  key: String,
  postalCodeComponent: Option[String],
  countryComponent: Option[String]
)
