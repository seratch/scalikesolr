addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.3.0")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.4")

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.6")

addSbtPlugin("com.github.seratch" % "xsbt-scalag-plugin" % "0.2.+")

addSbtPlugin("com.typesafe.sbt" % "sbt-pgp" % "0.8.3")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

