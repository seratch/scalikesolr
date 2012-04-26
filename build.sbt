import testgen.TestgenKeys._

scalaVersion := "2.9.2"

crossScalaVersions := Seq("2.9.2", "2.9.1", "2.9.0")

libraryDependencies <++= (scalaVersion) { scalaVersion =>
  Seq(
    "org.slf4j"                % "slf4j-api"          % "1.6.4"           % "compile",
    "joda-time"                % "joda-time"          % "1.6.2"           % "compile",
    "org.apache.solr"          % "solr-solrj"         % "3.6.0"           % "compile",
    "ch.qos.logback"           % "logback-classic"    % "1.0.0"           % "test",
    "junit"                    % "junit"              % "4.10"            % "test",
    "org.mockito"              % "mockito-all"        % "1.8.2"           % "test",
    "org.scalatest"            %% "scalatest"         % "1.7.1"           % "test",
    "org.scala-lang"           % "scala-compiler"     % scalaVersion      % "test"
  )
}

externalResolvers ~= (_.filter(_.name != "Scala-Tools Maven2 Repository"))

scalacOptions ++= Seq("-deprecation", "-unchecked")

// ls

seq(lsSettings :_*)

// scalariform

seq(scalariformSettings: _*)

// testgen

seq(testgenSettings: _*)

testgenEncoding in Compile := "UTF-8"

testgenTestTemplate in Compile := "scalatest.FlatSpec"

testgenScalaTestMatchers in Compile := "ShouldMatchers"

// publish

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { x => false }

pomExtra := (
  <url>http://seratch.github.com/scalikesolr</url>
  <licenses>
    <license>
      <name>Apache License, Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:seratch/scalikesolr.git</url>
    <connection>scm:git:git@github.com:seratch/scalikesolr.git</connection>
  </scm>
  <developers>
    <developer>
      <id>seratch</id>
      <name>Kazuhuiro Sera</name>
      <url>http://seratch.net/</url>
    </developer>
  </developers>
)

