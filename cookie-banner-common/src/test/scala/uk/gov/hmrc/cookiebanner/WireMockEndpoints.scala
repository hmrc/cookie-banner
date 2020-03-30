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

import java.net.ServerSocket

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.{MappingBuilder, ResponseDefinitionBuilder, WireMock}
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import com.github.tomakehurst.wiremock.http.RequestMethod
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Suite}

import scala.util.{Random, Try}

trait WireMockEndpoints extends Suite with BeforeAndAfterAll with BeforeAndAfterEach {

  val wireMockHost: String = "localhost"
  val wireMockPort: Int = PortTester.findPort()
  val wireMock = new WireMock(wireMockHost, wireMockPort)

  private val wireMockServer = new WireMockServer(wireMockConfig().port(wireMockPort))

  def startWireMock(): Unit = wireMockServer.start()
  def stopWireMock(): Unit = wireMockServer.stop()

  def partialEndpoint(url: String, willRespondWith: (Int, Option[String])): Unit = {
    val builder = new MappingBuilder(RequestMethod.GET, urlEqualTo(url))
    val response = new ResponseDefinitionBuilder()
      .withStatus(willRespondWith._1)
    val resp = willRespondWith._2.map(response.withBody).getOrElse(response)
    builder.willReturn(resp)

    wireMock.register(builder)
  }

  override def beforeEach(): Unit = {
    wireMock.resetMappings()
    wireMock.resetScenarios()
  }
  override def afterAll(): Unit =
    wireMockServer.stop()
  override def beforeAll(): Unit = {
    println(s"starting endpoint server on $wireMockPort")
    wireMockServer.start()
  }

}

private object PortTester {

  def findPort(excluded: Int*): Int =
    Random.shuffle((6001 to 7000).toVector).find(port => !excluded.contains(port) && isFree(port)).getOrElse(throw new Exception("No free port"))

  private def isFree(port: Int): Boolean = {
    val triedSocket = Try {
      val serverSocket = new ServerSocket(port)
      Try(serverSocket.close())
      serverSocket
    }
    triedSocket.isSuccess
  }
}