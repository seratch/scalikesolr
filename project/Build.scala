import sbt._
import Keys._

object ScalikeSolrBuild extends Build {

  lazy val root = Project("root", file("."), settings = mainSettings)

  lazy val mainSettings: Seq[Project.Setting[_]] = Defaults.defaultSettings ++ Seq(
    sbtPlugin := false,
    organization := "com.github.seratch",
    name := "scalikesolr",
    version := "3.6.2",
    scalaVersion := "2.9.2",
    crossScalaVersions := Seq("2.9.2", "2.9.1", "2.9.0"),
    libraryDependencies <++= (scalaVersion) { scalaVersion =>
      Seq(
        "org.slf4j"                % "slf4j-api"          % "1.6.4"           % "compile",
        "joda-time"                % "joda-time"          % "2.1"             % "compile",
        "org.joda"                 % "joda-convert"       % "1.2"             % "compile",
        "org.apache.solr"          % "solr-solrj"         % "3.6.0"           % "compile",
        "ch.qos.logback"           % "logback-classic"    % "1.0.0"           % "test",
        "junit"                    % "junit"              % "4.10"            % "test",
        "org.mockito"              % "mockito-all"        % "1.8.2"           % "test",
        "org.scalatest"            %% "scalatest"         % "1.7.1"           % "test",
        "org.scala-lang"           % "scala-compiler"     % scalaVersion      % "test"
      )
    },
    externalResolvers ~= (_.filter(_.name != "Scala-Tools Maven2 Repository")),
    publishTo <<= version { (v: String) =>
      val nexus = "https://oss.sonatype.org/"
        if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots") 
        else Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },
    publishMavenStyle := true,
    scalacOptions ++= Seq("-deprecation", "-unchecked"),
    publishArtifact in Test := false,
    pomIncludeRepository := { x => false },
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
  )

}

