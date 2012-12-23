import sbt._
import Keys._

object ScalikeSolrBuild extends Build {

  lazy val root = Project("root", file("."), settings = mainSettings)

  lazy val mainSettings: Seq[Project.Setting[_]] = Defaults.defaultSettings ++ Seq(
    sbtPlugin := false,
    organization := "com.github.seratch",
    name := "scalikesolr",
    version := "4.0.0",
    scalaVersion := "2.10.0",
    crossScalaVersions := Seq("2.10.0", "2.9.2"),
    libraryDependencies <++= (scalaVersion) { scalaVersion =>
      val _scalaVersion = "_" + (scalaVersion match {
        case "2.10.0" => "2.10.0"
        case version => version
      })
      val scalatest = "scalatest" + _scalaVersion
      Seq(
        "org.slf4j"                % "slf4j-api"          % "1.7.2"           % "compile",
        "joda-time"                % "joda-time"          % "2.1"             % "compile",
        "org.joda"                 % "joda-convert"       % "1.2"             % "compile",
        "org.apache.solr"          % "solr-solrj"         % "4.0.0"           % "compile",
        "ch.qos.logback"           % "logback-classic"    % "1.0.7"           % "test",
        "junit"                    % "junit"              % "4.10"            % "test",
        "org.mockito"              % "mockito-all"        % "1.9.0"           % "test",
        "org.scalatest"            % scalatest            % "1.8"             % "test",
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

