import sbt._

object AppDependencies {

  lazy val test: Seq[ModuleID] = Seq(
    "org.scalatest"        %% "scalatest"                % "3.1.0"        % Test,
    "com.vladsch.flexmark" % "flexmark-all"              % "0.35.10"      % Test,
    "ch.qos.logback"       % "logback-classic"           % "1.2.3"        % Test,
  )

  lazy val cookieBannerPlay25: Seq[ModuleID] = Seq(
    "uk.gov.hmrc" %% "play-partials" % "6.9.0-play-25"
  ) ++ test

  lazy val cookieBannerPlay26: Seq[ModuleID] = Seq(
    "uk.gov.hmrc" %% "play-partials" % "6.9.0-play-26"
  ) ++ test
}

