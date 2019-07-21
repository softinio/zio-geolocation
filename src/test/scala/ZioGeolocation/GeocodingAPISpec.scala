package ZioGeolocation

import org.specs2.Specification
import zio._

class GeocodingAPISpec extends Specification with DefaultRuntime {
  def is = "GetAddressSpec".title ^ s2"""
    should fail getting a location with error REQUEST_DENIED $e1

    """
  def e1 = {
    val sampleAddress: String = "44 Montgomery St, San Francisco, CA 94104"
    val request: GeoRequest = GeoRequest(
      address = sampleAddress,
      key = "",
      postalCodeComponent = None,
      countryComponent = None
    )
    unsafeRun(for {
      locations <- GeocodingAPI.getLocation(request).flip
    } yield (locations.getMessage must_=== "REQUEST_DENIED"))
  }
}
