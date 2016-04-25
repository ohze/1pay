import play.core.PlayVersion.{current => playVersion}

lazy val commonSettings = Seq(
  organization := "com.sandinh",
  version := "2.0.1",
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-feature", "-target:jvm-1.8", "-Ybackend:GenBCode"),
  resolvers += "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases"
)

lazy val core = project
  .settings(commonSettings: _*)
  .settings(
    name := "1pay",
    libraryDependencies ++= Seq(jdbc,
      "com.typesafe.play" %% "play"         % playVersion,
      "com.typesafe.play" %% "anorm"        % "2.5.0",
      "org.scalatest"     %% "scalatest"    % "3.0.0-M15"   % Test
    )
  )

lazy val example = project
  .enablePlugins(PlayScala)
  .settings(commonSettings: _*)
  .settings(
    name := "example",
    libraryDependencies ++= Seq(cache,
      "mysql"                 %  "mysql-connector-java"     % "5.1.38" % Runtime,
      "com.sandinh"           %% "subfolder-evolutions"     % "2.4.6"  % Test,
      specs2 % Test
    ),
    routesGenerator := InjectedRoutesGenerator,
    publishArtifact := false
  ).dependsOn(core)

lazy val root = project.in(file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "root",
    publishArtifact := false
  ).aggregate(core, example)
