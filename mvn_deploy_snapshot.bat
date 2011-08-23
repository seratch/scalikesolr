@echo off
mvn clean -DaltDeploymentRepository=snapshot-repo::default::file:./mvn-repo/snapshots clean deploy
