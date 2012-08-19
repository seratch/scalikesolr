#!/bin/sh
java -Xmx1024M -XX:MaxPermSize=256M -jar `dirname $0`/lib/sbt-launch.jar "$@"

