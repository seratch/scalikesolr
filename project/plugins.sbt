resolvers ++= Seq(
  "seratch.github.com releases"  at "http://seratch.github.com/mvn-repo/releases",
  "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"
)

addSbtPlugin("com.github.seratch" %% "testgen-sbt" % "0.3.0")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.0.0")

resolvers ++= Seq(
  "less is" at "http://repo.lessis.me",
  "coda" at "http://repo.codahale.com")

addSbtPlugin("me.lessis" % "ls-sbt" % "0.1.1")

resolvers += Classpaths.typesafeResolver

addSbtPlugin("com.typesafe.sbtscalariform" % "sbtscalariform" % "0.3.0")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.0.0")

// for sonatype publishment

resolvers += Resolver.url("sbt-plugin-releases", new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)

addSbtPlugin("com.jsuereth" % "xsbt-gpg-plugin" % "0.5")

