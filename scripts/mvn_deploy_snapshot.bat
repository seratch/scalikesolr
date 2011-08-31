@echo off
cd %~dp0\../.
mvn -f pom.xml clean -DaltDeploymentRepository=release-repo::default::file:./mvn-repo/snapshots clean deploy
