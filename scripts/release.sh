#!/bin/sh

sbt ++2.10.0 publish-signed "g scala2.9" ++2.9.2 publish-signed ++2.9.3 publish-signed
git reset --hard

