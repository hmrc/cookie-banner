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

import javax.inject.Inject
import play.api.Configuration

import scala.concurrent.duration.Duration

class CookieBannerConfig @Inject() (configuration: Configuration) extends CommonConfig {
  def partialUrl: Option[String] = {
    val protocol = configuration.getOptional[String]("cookie-banner.protocol").getOrElse("https")
    val port     = configuration.getOptional[Int]("cookie-banner.port").getOrElse(443)

    for {
      host <- configuration.getOptional[String]("cookie-banner.host")
      path <- configuration.getOptional[String]("cookie-banner.path")
    } yield {
      s"$protocol://$host:$port$path"
    }
  }

  def cacheRefreshAfter: Option[Duration] =
    configuration.getOptional[Duration]("cookie-banner.refreshAfter")

  def cacheExpireAfter: Option[Duration] =
    configuration.getOptional[Duration]("cookie-banner.expireAfter")
}
