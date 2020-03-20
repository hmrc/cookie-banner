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

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import play.api.Configuration

import scala.concurrent.duration._

class CookieBannerConfigSpec extends AnyFreeSpec with Matchers {

  "CookieBannerConfig" - {
    "partialUrl" - {
      "should return value with all options configured" in {
        val config = Configuration(
          "cookie-banner.protocol" -> "http",
          "cookie-banner.host" -> "localhost",
          "cookie-banner.port" -> "8080",
          "cookie-banner.path" -> "/get-my-cookies"
        )
        val underTest = new CookieBannerConfig(config)
        underTest.partialUrl shouldBe Some("http://localhost:8080/get-my-cookies")
      }

      "return None if host is missing" in {
        val config = Configuration(
          "cookie-banner.protocol" -> "http",
          "cookie-banner.port" -> "8080",
          "cookie-banner.path" -> "/get-my-cookies"
        )
        val underTest = new CookieBannerConfig(config)
        underTest.partialUrl shouldBe None
      }

      "return None if path is missing" in {
        val config = Configuration(
          "cookie-banner.protocol" -> "http",
          "cookie-banner.host" -> "localhost",
          "cookie-banner.port" -> "8080"
        )
        val underTest = new CookieBannerConfig(config)
        underTest.partialUrl shouldBe None
      }

      "default protocol if missing" in {
        val config = Configuration(
          "cookie-banner.host" -> "localhost",
          "cookie-banner.port" -> "8080",
          "cookie-banner.path" -> "/get-my-cookies"
        )
        val underTest = new CookieBannerConfig(config)
        underTest.partialUrl shouldBe Some("https://localhost:8080/get-my-cookies")
      }

      "default port if missing" in {
        val config = Configuration(
          "cookie-banner.protocol" -> "http",
          "cookie-banner.host" -> "localhost",
          "cookie-banner.path" -> "/get-my-cookies"
        )
        val underTest = new CookieBannerConfig(config)
        underTest.partialUrl shouldBe Some("http://localhost:443/get-my-cookies")
      }
    }

    "cacheRefreshAfter" - {
      "should load from config" in {
        val config = Configuration(
          "cookie-banner.refreshAfter" -> "1 minute"
        )
        new CookieBannerConfig(config).cacheRefreshAfter shouldBe Some(1 minute)
      }

      "return None if not configured" in {
        val config = Configuration.empty
        new CookieBannerConfig(config).cacheRefreshAfter shouldBe None
      }
    }

    "cacheExpireAfter" - {
      "should load from config" in {
        val config = Configuration(
          "cookie-banner.expireAfter" -> "1 minute"
        )
        new CookieBannerConfig(config).cacheExpireAfter shouldBe Some(1 minute)
      }

      "return None if not configured" in {
        val config = Configuration.empty
        new CookieBannerConfig(config).cacheExpireAfter shouldBe None
      }
    }
  }
}
