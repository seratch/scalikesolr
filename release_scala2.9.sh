#!/bin/sh

sbt "g scala2.9" ++2.9.2 publish ++2.9.3 publish
git reset --hard

