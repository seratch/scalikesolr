resolvers ++= Seq(
  "seratch.github.com releases"  at "http://seratch.github.com/mvn-repo/releases"
)

addSbtPlugin("com.github.seratch" %% "testgen-sbt" % "0.1")

