import play.core.PlayVersion.{current => playVersion}

lazy val commonSettings = Seq(
  organization := "com.sandinh",
  version := "1.0.3-SNAPSHOT",
  scalaVersion := "2.11.7",
  scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-feature", "-target:jvm-1.8", "-Ybackend:GenBCode"),
  resolvers += "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases"
)

lazy val core = project.in(file("core"))
  .settings(commonSettings: _*)
  .settings(
    name := "1pay",
    libraryDependencies ++= Seq(jdbc,
      "com.typesafe.play" %% "play"         % playVersion,
      "com.typesafe.play" %% "anorm"        % "2.5.0",
      "org.scalatest"     %% "scalatest"    % "3.0.0-M14"   % Test
    )
  )

lazy val example = project.in(file("example"))
  .enablePlugins(PlayScala)
  .settings(commonSettings: _*)
  .settings(
    name := "example",
    libraryDependencies ++= Seq(cache,
      "mysql"                 %  "mysql-connector-java"     % "5.1.38" % Runtime,
      "com.sandinh"           %% "subfolder-evolutions"     % "2.4.3"  % Test,
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
