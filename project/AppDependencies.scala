import sbt._

object AppDependencies {

  lazy val testCommon: Seq[ModuleID] = Seq(
    "org.scalatest"          %% "scalatest"          % "3.1.0"    % Test,
    "com.vladsch.flexmark"   %  "flexmark-all"       % "0.35.10"  % Test,
    "ch.qos.logback"         %  "logback-classic"    % "1.2.3"    % Test,
    "com.github.tomakehurst" %  "wiremock"           % "1.58"     % Test,
  )

  lazy val cookieBannerCommon: Seq[ModuleID] = testCommon

  // https://github.com/playframework/scalatestplus-play#releases
  lazy val cookieBannerPlay25: Seq[ModuleID] = Seq(
    "uk.gov.hmrc" %% "play-partials" % "6.9.0-play-25",
  ) ++ testCommon ++ Seq(
    "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.1" % Test,
  )

  lazy val cookieBannerPlay26: Seq[ModuleID] = Seq(
    "uk.gov.hmrc" %% "play-partials" % "6.9.0-play-26"
  ) ++ testCommon ++ Seq(
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.3" % Test,
  )
}
