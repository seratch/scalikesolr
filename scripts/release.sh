#!/bin/sh

sbt ++2.10.0 publish "g scala2.9" ++2.9.2 publish ++2.9.3 publish
git reset --hard

