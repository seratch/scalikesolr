import sbt._
import Keys._

object ScalikeSolrBuild extends Build {

  lazy val root = Project("root", file("."), settings = mainSettings)

  lazy val mainSettings: Seq[Project.Setting[_]] = Defaults.defaultSettings ++ Seq(
    sbtPlugin := false,
    organization := "com.github.seratch",
    name := "scalikesolr",
    version := "3.5.0",
    /*
    publishTo <<= (version) { version: String =>
      Some(
        Resolver.file("GitHub Pages", Path.userHome / "github" / "seratch.github.com" / "mvn-repo" / {
          if (version.trim.endsWith("SNAPSHOT")) "snapshots" else "releases" 
        })
      )
    },
    */
    publishTo <<= version { (v: String) =>
      val nexus = "https://oss.sonatype.org/"
        if (v.trim.endsWith("SNAPSHOT")) 
          Some("snapshots" at nexus + "content/repositories/snapshots") 
        else
          Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },
    publishMavenStyle := true,
    scalacOptions ++= Seq("-deprecation", "-unchecked")
  )

}

