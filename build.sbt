lazy val core = projectMatrix
  .playAxis(
    play26,
    Seq(scala212),
    _.settings(
      mimaPreviousArtifacts := Set(
        "com.sandinh" %% "1pay" % "2.3.0",
      ),
      libraryDependencySchemes ++= Seq(
        // change from 22.0 to 23.6.1-jre
        "com.google.guava" % "guava" % "always",
        // change from 0.2.1 to 0.3.8
        "com.typesafe" %% "ssl-config-core" % "always",
        // change from 1.0.6 to 1.1.2
        "org.scala-lang.modules" %% "scala-parser-combinators" % "semver-spec",
        // change from 1.8.1 to 2.2.1
        "org.joda" % "joda-convert" % "always",
      ),
      versionPolicyIgnored := Seq(
        "com.jsuereth" %% "scala-arm", // missing
        "com.typesafe.play" %% "anorm-tokenizer", // missing
        "com.typesafe.play" %% "anorm" // use org.playframework.anorm:anorm
      ),
    )
  )
  .playAxis(
    play28,
    Seq(scala212, scala213),
    _.settings(
      versionPolicyFirstVersion := Some("2.5.0"),
    )
  )
  .settings(
    name := "1pay",
    libraryDependencies ++= Seq(
      "org.playframework.anorm" %% "anorm" % "2.6.10",
      "commons-codec" % "commons-codec" % "1.15",
    ) ++ play("jdbc").value ++ scalatest("-flatspec", "-shouldmatchers"),
  )

import _root_.play.sbt.PlayImport
lazy val example = project
  .enablePlugins(PlayScala)
  .settings(
    skipPublish,
    libraryDependencies ++= Seq(
      ehcache,
      "mysql" % "mysql-connector-java" % "5.1.49" % Runtime,
      "com.sandinh" %% "subfolder-evolutions" % "2.8.1" % Test,
      PlayImport.specs2 % Test,
    ),
    routesGenerator := InjectedRoutesGenerator,
    addOpensForTest(),
  )
  .dependsOn(core.finder(play28)(scala212))

lazy val sms1pay = (project in file("."))
  .settings(skipPublish)
  .aggregate(core.projectRefs :+ (example: ProjectReference): _*)

inThisBuild(
  Seq(
    developers := List(
      Developer(
        "thanhbv",
        "Bui Viet Thanh",
        "thanhbv@sandinh.net",
        url("https://sandinh.com")
      ),
    ),
  )
)
