package ZioGeolocation

import zio.{ TaskR, ZIO }

package object geocoding extends Geocoding.Service[Geocoding] {
  override def get(
    address: String,
    postalCode: Option[String],
    countryCode: Option[String],
    settings: GeocodingSettings
  ): TaskR[Geocoding, List[Response]] =
    ZIO.accessM(
      _.userGeocoding.get(
        address,
        postalCode,
        countryCode,
        settings
      )
    )
}
