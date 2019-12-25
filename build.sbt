import play.core.PlayVersion.{current => playVersion}

lazy val commonSettings = Seq(
  organization := "com.sandinh",
  version := "2.4.0-SNAPSHOT",
  scalaVersion := "2.13.1",
  crossScalaVersions := Seq("2.13.1", "2.12.10"),
  scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-feature"),
  scalacOptions ++= (CrossVersion.scalaApiVersion(scalaVersion.value) match {
    case Some((2, 12)) => Seq("-target:jvm-1.8")
    case _ => Nil
  })
)

lazy val core = project
  .settings(commonSettings: _*)
  .settings(
    name := "1pay",
    libraryDependencies ++= Seq(jdbc,
      "com.typesafe.play" %% "play"         % playVersion,
      "org.playframework.anorm" %% "anorm"  % "2.6.5",
      "commons-codec"     % "commons-codec" % "1.13",
      "org.scalatest"     %% "scalatest"    % "3.1.0"   % Test,
    )
  )

lazy val example = project
  .enablePlugins(PlayScala)
  .settings(commonSettings: _*)
  .settings(
    resolvers += Resolver.sonatypeRepo("releases"),
    libraryDependencies ++= Seq(ehcache,
      "mysql"                 %  "mysql-connector-java"     % "5.1.48" % Runtime,
      "com.sandinh"           %% "subfolder-evolutions"     % "2.8.0"  % Test,
      specs2 % Test
    ),
    routesGenerator := InjectedRoutesGenerator,
    publishArtifact := false
  ).dependsOn(core)

lazy val sms1pay = project.in(file("."))
  .settings(commonSettings: _*)
  .settings(
    publishArtifact := false
  ).aggregate(core, example)
