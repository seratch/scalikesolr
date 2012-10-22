externalResolvers ~= (_.filter(_.name != "Scala-Tools Maven2 Repository"))

addSbtPlugin("com.github.seratch" %% "testgenerator" % "1.1.0")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.1.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.0.0")

// for sonatype publishment

addSbtPlugin("com.jsuereth" % "xsbt-gpg-plugin" % "0.6")

