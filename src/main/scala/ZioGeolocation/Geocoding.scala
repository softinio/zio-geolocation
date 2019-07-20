package ZioGeolocation

import zio._

object Geocoding {
  private def createResponse(location: Results): Response = {
      Response(
        address = location.formattedAddress,
        quality = location.geometry.locationType,
      )
  }

  def apply(
    address: String,
    postalCode: Option[String],
    countryCode: Option[String],
    settings: GeocodingSettings
  ): IO[String, List[Response]] = {
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
