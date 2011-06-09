@echo off
mvn -f pom_2.8.1.xml clean -DaltDeploymentRepository=release-repo::default::file:./mvn-repo/releases clean deploy
