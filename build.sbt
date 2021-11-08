lazy val core = projectMatrix
  .playAxis(play26, Seq(scala212))
  .playAxis(play28, Seq(scala212, scala213))
  .settings(
    name := "1pay",
    libraryDependencies ++= Seq(
      "org.playframework.anorm" %% "anorm" % "2.6.10",
      "commons-codec" % "commons-codec" % "1.15",
      "org.scalatest" %% "scalatest" % "3.1.4" % Test,
    ) ++ play("jdbc").value,
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
