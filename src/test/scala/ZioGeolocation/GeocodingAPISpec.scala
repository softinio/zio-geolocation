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

import zio.test.Assertion._
import zio.test._

object GeocodingAPISpec extends DefaultRunnableSpec {
  def spec = suite("GeocodingAPISpec")(
    testM("Calling API and Being Denied Access") {
    val sampleAddress: String = "44 Montgomery St, San Francisco, CA 94104"
    val request: GeoRequest = GeoRequest(
      address = sampleAddress,
      key = "",
      postalCodeComponent = None,
      countryComponent = None
    )
    for {
      locations <- GeocodingAPI.getLocation(request).flip
    } yield assert(locations.getMessage)(equalTo("REQUEST_DENIED"))
    }
  )
}
