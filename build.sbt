name := "scalikesolr"

organization := "com.github.seratch"

scalaVersion := "2.9.1"

crossScalaVersions := Seq("2.9.1", "2.9.0-1", "2.9.0")

libraryDependencies <++= (scalaVersion) { scalaVersion =>
  val specsArtifactId = scalaVersion match {
    case "2.9.1" => "specs_2.9.0"
    case _       => "specs_" + scalaVersion
  }
  Seq(
    "joda-time"                % "joda-time"          % "1.6.2"           % "provided",
    "ch.qos.logback"           % "logback-classic"    % "0.9.26"          % "provided",
    "org.apache.solr"          % "solr-solrj"         % "3.5.0"           % "provided",
    "junit"                    % "junit"              % "4.9"             % "test",
    "org.mockito"              % "mockito-all"        % "1.8.2"           % "test",
    "org.scalatest"            %% "scalatest"         % "1.6.1"           % "test",
    "org.scala-tools.testing"  % specsArtifactId      % "1.6.8"           % "test",
    "org.scala-lang"           % "scala-compiler"     % scalaVersion      % "test"
  )
}

scalacOptions ++= Seq("-deprecation", "-unchecked")

//defaultExcludes ~= (_ || "*~")
