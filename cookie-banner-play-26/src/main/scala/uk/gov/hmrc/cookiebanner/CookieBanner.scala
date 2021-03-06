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
import play.api.{Configuration, Logger}
import play.api.mvc.RequestHeader
import play.twirl.api.Html
import uk.gov.hmrc.http.CoreGet
import uk.gov.hmrc.play.partials.CachedStaticHtmlPartialRetriever

import scala.concurrent.duration.Duration

class CookieBanner @Inject() (http: CoreGet, config: Configuration) extends CachedStaticHtmlPartialRetriever {

  private val cookieBannerConfig = new CookieBannerConfig(config)

  override def httpGet: CoreGet = http

  override def refreshAfter: Duration =
    cookieBannerConfig.cacheRefreshAfter.getOrElse(super.refreshAfter)

  override def expireAfter: Duration =
    cookieBannerConfig.cacheExpireAfter.getOrElse(super.expireAfter)

  def cookieBanner(implicit req: RequestHeader): Html =
    cookieBannerConfig.partialUrl
      .map(loadPartial(_).successfulContentOrEmpty)
      .getOrElse {
        Logger.logger.warn("cookie-banner is not configured")
        Html("")
      }
}
