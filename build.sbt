import play.core.PlayVersion.{current => playVersion}
import scalariform.formatter.preferences._

lazy val formatSetting = scalariformPreferences := scalariformPreferences.value
  .setPreference(AlignParameters, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(MultilineScaladocCommentsStartOnFirstLine, true)
  .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)
  .setPreference(SpacesAroundMultiImports, false)

lazy val commonSettings = formatSetting +: Seq(
  organization := "com.sandinh",
  version := "2.2.0",
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-feature", "-target:jvm-1.8", "-Ybackend:GenBCode")
)

lazy val core = project
  .settings(commonSettings: _*)
  .settings(
    name := "1pay",
    libraryDependencies ++= Seq(jdbc,
      "com.typesafe.play" %% "play"         % playVersion,
      "com.typesafe.play" %% "anorm"        % "2.5.1",
      "org.scalatest"     %% "scalatest"    % "3.0.0-RC1"   % Test
    )
  )

lazy val example = project
  .enablePlugins(PlayScala)
  .settings(commonSettings: _*)
  .settings(
    name := "example",
    libraryDependencies ++= Seq(cache,
      "mysql"                 %  "mysql-connector-java"     % "5.1.39" % Runtime,
      "com.sandinh"           %% "subfolder-evolutions"     % "2.5.2"  % Test,
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
