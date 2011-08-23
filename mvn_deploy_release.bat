@echo off
mvn clean -DaltDeploymentRepository=release-repo::default::file:./mvn-repo/releases clean deploy
