package ZioGeolocation

import io.circe.generic.auto._, io.circe.syntax._
import com.softwaremill.sttp._
import com.softwaremill.sttp.circe._
import com.vitorsvieira.iso._
import zio._

/**
 *
 * Service calling Google's Geocoding API
 * https://developers.google.com/maps/documentation/geocoding/start
 *
 */
object GeocodingAPI {
  implicit val backend       = HttpURLConnectionBackend()
  val geocodeUrl: String     = "https://maps.googleapis.com/maps/api/geocode/json"

  def getLocation(requestDetails: GeoRequest): IO[String, GeoResponse] = {
    val validCountryCode: Option[String] = requestDetails.countryComponent.map(ISOCountry(_).toString)
    val url =
      uri"$geocodeUrl?address=$requestDetails.address&key=$requestDetails.key&postal_code=$requestDetails.postalCodeComponent&country=$validCountryCode"
    val response = sttp
      .get(url)
      .response(asJson[GeoResponse])
      .send()

    response.body match {
      case Left(error)   => ZIO.fail(error)
      case Right(received) => {
        received match {
          case Left(jsonError) => ZIO.fail(jsonError.message)
          case Right(parsed) => {
            if (parsed.results.isEmpty) ZIO.fail(parsed.status)
            else ZIO.succeed(parsed)
          }
        }
      }
    }
  }
}
