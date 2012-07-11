#!/bin/sh

if [ $# -eq 0 ]; then
  echo "[usage] $0 [vesrion]"
  exit 1
fi

VERSION=$1
curl -X POST http://ls.implicit.ly/api/1/libraries -d 'user=seratch&repo=scalikesolr&version='$VERSION

echo "Done."

