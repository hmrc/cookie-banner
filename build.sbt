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
  crossScalaVersions := Seq(scala2_11), // todo - update to include scala2_12, when play-partials is on 2.12 too.
  makePublicallyAvailableOnBintray := true,
  resolvers := commonResolvers
)

lazy val library = Project(name, file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(
    commonSettings,
    publish := {},
    publishAndDistribute := {},

    // by default this is Seq(scalaVersion) which doesn't play well and causes sbt
    // to try to an invalid cross-build
    crossScalaVersions := Seq.empty
  )
  .aggregate(
    cookieBannerPlay25,
    cookieBannerPlay26
  )

lazy val cookieBannerPlay25 = Project("cookie-banner-play-25", file("cookie-banner-play-25"))
  .enablePlugins(SbtAutoBuildPlugin, SbtArtifactory)
  .settings(
    commonSettings,
    crossScalaVersions := Seq(scala2_11),
    libraryDependencies ++= AppDependencies.cookieBannerPlay25
  )

lazy val cookieBannerPlay26 = Project("cookie-banner-play-26", file("cookie-banner-play-26"))
  .enablePlugins(SbtAutoBuildPlugin, SbtArtifactory)
  .settings(
    commonSettings,
    libraryDependencies ++= AppDependencies.cookieBannerPlay26
  )