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
import javax.inject.Inject
import play.api.mvc.RequestHeader
import play.api.{Configuration, Logger, Play}
import play.twirl.api.Html
import uk.gov.hmrc.http.hooks.HttpHook
import uk.gov.hmrc.http.{CoreGet, HttpGet}
import uk.gov.hmrc.play.http.ws.WSGet
import uk.gov.hmrc.play.partials.CachedStaticHtmlPartialRetriever

case class CookieConfig(protocol: String, host: String, port: Int, path: String)

class CookieBanner @Inject() (http: WSGet, config: Configuration) extends CachedStaticHtmlPartialRetriever {
  override def httpGet: CoreGet = http
  private val partialUrl: Option[String] = config.getString("cookie-banner-url")

  def cookieBanner(implicit req: RequestHeader): Html =
    partialUrl
      .map(loadPartial(_).successfulContentOrEmpty)
      .getOrElse {
        Logger.logger.warn("cookie-banner-url is not configured")
        Html("")
      }
}

object WSHttp extends HttpGet with WSGet {
  override protected def actorSystem: ActorSystem = Play.current.actorSystem
  override protected def configuration: Option[Config] = Some(Play.current.configuration.underlying)
  override val hooks: Seq[HttpHook] = NoneRequired
}

object CookieBannerStatic extends CachedStaticHtmlPartialRetriever {
  private val config = Play.current.configuration
  override def httpGet: CoreGet = WSHttp
  private val partialUrl = config.getString("cookie-banner-url")

  def cookieBanner(implicit req: RequestHeader): Html =
    partialUrl
      .map(loadPartial(_).successfulContentOrEmpty)
      .getOrElse {
        Logger.logger.warn("cookie-banner-url is not configured")
        Html("")
      }
}
