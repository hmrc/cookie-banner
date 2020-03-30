/*
 * Copyright 2020 HM Revenue & Customs
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

package uk.gov.hmrc.cookiebanner

import akka.actor.ActorSystem
import com.typesafe.config.Config
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Configuration
import play.api.libs.ws.WSClient
import play.api.mvc.RequestHeader
import play.api.test.FakeRequest
import play.twirl.api.Html
import uk.gov.hmrc.http.hooks.HttpHook
import uk.gov.hmrc.http._
import uk.gov.hmrc.play.http.ws.WSHttp

class CookieBannerSpec extends AnyFreeSpec with Matchers
  with WireMockEndpoints with GuiceOneAppPerSuite {

  private val ws = app.injector.instanceOf[WSClient]
  private val actorSystem = app.injector.instanceOf[ActorSystem]

  private val config = Configuration(
    "cookie-banner.host" -> wireMockHost,
    "cookie-banner.port" -> wireMockPort,
    "cookie-banner.path" -> "/tracking-consent",
    "cookie-banner.protocol" -> "http"
  )

  private def cookieBanner(config: Configuration) = {
    implicit val fakeRequest: RequestHeader = FakeRequest()
    // create a new CookieBanner for each test, to avoid caching
    new CookieBanner(new DefaultHttpClient(config, ws, actorSystem), config).cookieBanner
  }

  "CookieBanner" - {
    "should retrieve partial" in {
      partialEndpoint("/tracking-consent",
        willRespondWith = (
          200,
          Some("<p>Some partial content</p>")))

      cookieBanner(config) shouldBe Html("<p>Some partial content</p>")
    }

    "should return empty Html on error from cookie-banner-url" in {
      partialEndpoint("/tracking-consent",
        willRespondWith = (
          404,
          None))

      cookieBanner(config) shouldBe Html("")
    }

    "should return empty Html if configuration is not set" in {
      cookieBanner(Configuration.empty) shouldBe Html("")
    }
  }
}

// this snippet is taken from play-bootstrap. we don't want a dependency on play-bootstrap in this library though
trait HttpClient extends HttpGet with HttpPut with HttpPost with HttpDelete with HttpPatch
class DefaultHttpClient(config: Configuration,
                        override val wsClient: WSClient,
                        override protected val actorSystem: ActorSystem) extends HttpClient with WSHttp {
  override lazy val configuration: Option[Config] = Option(config.underlying)
  override val hooks: Seq[HttpHook] = Seq()
}
