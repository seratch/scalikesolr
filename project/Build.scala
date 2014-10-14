import sbt._
import Keys._

object ScalikeSolrBuild extends Build {

  lazy val root = Project("root", file("."), settings = mainSettings)

  val solrVersion = "4.10.1"

  lazy val mainSettings = Seq(
    organization := "com.github.seratch",
    name := "scalikesolr",
    version := solrVersion,
    scalaVersion := "2.10.4",
    crossScalaVersions := Seq("2.10.4", "2.11.2"),
    libraryDependencies <++= (scalaVersion) { scalaVersion =>
      Seq(
        "org.slf4j"                %  "slf4j-api"       % "1.7.7"       % "compile",
        "joda-time"                %  "joda-time"       % "2.5"         % "compile",
        "org.joda"                 %  "joda-convert"    % "1.7"         % "compile",
        "org.apache.solr"          %  "solr-solrj"      % solrVersion   % "compile",
        "ch.qos.logback"           %  "logback-classic" % "1.1.2"       % "test",
        "junit"                    %  "junit"           % "4.11"        % "test",
        "org.mockito"              %  "mockito-all"     % "1.10.8"       % "test",
        "org.scalatest"            %% "scalatest"       % "2.2.2"       % "test"
      ) ++ (scalaVersion match {
        case v if v.startsWith("2.11.") => Seq("org.scala-lang.modules" % "scala-xml_2.11" % "1.0.2" % "compile")
        case _ => Seq()
      })
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

