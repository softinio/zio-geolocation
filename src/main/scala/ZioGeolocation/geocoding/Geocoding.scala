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
