import play.core.PlayVersion.{current => playVersion}

name := "1pay"

organization := "com.sandinh"

version := "1.0.0"

scalaVersion := "2.11.7"

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-feature", "-target:jvm-1.8", "-Ybackend:GenBCode")

libraryDependencies ++= Seq(jdbc,
  "com.typesafe.play" %% "play"   % playVersion,
  "com.typesafe.play" %% "anorm"  % "2.5.0"
)
