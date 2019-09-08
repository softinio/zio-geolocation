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
