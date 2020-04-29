import sbt.Keys._
import sbt._

val name = "cookie-banner"

val scala2_11 = "2.11.12"
val scala2_12 = "2.12.10"

lazy val commonResolvers = Seq(
    Resolver.bintrayRepo("hmrc", "releases"),
    Resolver.typesafeRepo("releases")
)

lazy val commonSettings = Seq(
  organization := "uk.gov.hmrc",
  majorVersion := 0,
  scalaVersion := scala2_11,
  crossScalaVersions := Seq(scala2_11, scala2_12),
  makePublicallyAvailableOnBintray := true,
  resolvers := commonResolvers
)

lazy val library = Project(name, file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(
    commonSettings,
    publish := {},
    publishAndDistribute := {},
    crossScalaVersions := Seq.empty
  )
  .aggregate(
    cookieBannerCommon,
    cookieBannerPlay25,
    cookieBannerPlay26,
    cookieBannerPlay27
  )

lazy val cookieBannerCommon = Project("cookie-banner-common", file("cookie-banner-common"))
  .enablePlugins(SbtAutoBuildPlugin, SbtArtifactory)
  .settings(
    commonSettings,
    publish := {},
    publishAndDistribute := {},
    libraryDependencies ++= AppDependencies.cookieBannerCommon
  )

lazy val cookieBannerPlay25 = Project("cookie-banner-play-25", file("cookie-banner-play-25"))
  .enablePlugins(SbtAutoBuildPlugin, SbtArtifactory)
  .settings(
    commonSettings,
    scalaVersion := scala2_11,
    crossScalaVersions := Seq(scala2_11),
    libraryDependencies ++= AppDependencies.cookieBannerPlay25
  ).dependsOn(cookieBannerCommon % "test->test")

lazy val cookieBannerPlay26 = Project("cookie-banner-play-26", file("cookie-banner-play-26"))
  .enablePlugins(SbtAutoBuildPlugin, SbtArtifactory)
  .settings(
    commonSettings,
    libraryDependencies ++= AppDependencies.cookieBannerPlay26
  ).dependsOn(cookieBannerCommon % "test->test")

lazy val cookieBannerPlay27 = Project("cookie-banner-play-27", file("cookie-banner-play-27"))
  .enablePlugins(SbtAutoBuildPlugin, SbtArtifactory)
  .settings(
    commonSettings,
    Compile / scalaSource := (cookieBannerPlay26 / Compile / scalaSource).value,
    Test    / scalaSource := (cookieBannerPlay26 / Test    / scalaSource).value,
    libraryDependencies ++= AppDependencies.cookieBannerPlay27
  ).dependsOn(cookieBannerCommon % "test->test")
