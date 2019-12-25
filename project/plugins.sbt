addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.8.1")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.0.0")
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.0")

// sbt-dependency-graph should only be enabled when we want to inspect the dependencies
// after that, pls re-comment the following line when commit code
//addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.10.0-RC1")
