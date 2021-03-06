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

import play.api.Configuration

import scala.concurrent.duration._

class CookieBannerConfig(configuration: Configuration) {
  def partialUrl: Option[String] = {
    val hostOpt: Option[String] = configuration.getString("cookie-banner.host")
    val pathOpt: Option[String] = configuration.getString("cookie-banner.path")

    val protocol = configuration.getString("cookie-banner.protocol").getOrElse("https")
    val port     = configuration.getInt("cookie-banner.port").getOrElse(443)

    (hostOpt, pathOpt) match {
      case (Some(host), Some(path)) =>
        Some(s"$protocol://$host:$port$path")
      case _ => None
    }
  }

  def cacheRefreshAfter: Option[Duration] =
    configuration.getMilliseconds("cookie-banner.refreshAfter").map(_.millisecond)

  def cacheExpireAfter: Option[Duration] =
    configuration.getMilliseconds("cookie-banner.expireAfter").map(_.millisecond)
}
