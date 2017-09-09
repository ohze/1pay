import play.core.PlayVersion.{current => playVersion}

lazy val commonSettings = Seq(
  organization := "com.sandinh",
  version := "2.3.0",
  scalaVersion := "2.12.3",
  crossScalaVersions := Seq("2.11.11", "2.12.3"),
  scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-feature", "-target:jvm-1.8"),
  scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 11)) => Seq("-Ybackend:GenBCode")
    case _ => Nil
  })
)

lazy val core = project
  .settings(commonSettings: _*)
  .settings(
    name := "1pay",
    libraryDependencies ++= Seq(jdbc,
      "com.typesafe.play" %% "play"         % playVersion,
      "com.typesafe.play" %% "anorm"        % "2.5.3",
      "org.scalatest"     %% "scalatest"    % "3.0.4"   % Test
    )
  )

lazy val example = project
  .enablePlugins(PlayScala)
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(ehcache,
      "mysql"                 %  "mysql-connector-java"     % "5.1.44" % Runtime,
      evolutions  % Test,
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
