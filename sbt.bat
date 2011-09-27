@echo off
set SCRIPT_DIR=%~dp0
java -Xmx1024M -XX:MaxPermSize=128M -jar "%SCRIPT_DIR%\lib\sbt-launch.jar" %*
