package ZioGeolocation

/**
 *
 * Algrebra Data Types descibing the request received via http
 *
 */
final case class Request(
  address: String,
  postalCode: Option[String],
  countryCode: Option[String]
)

