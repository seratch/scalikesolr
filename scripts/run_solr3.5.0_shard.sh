#!/bin/sh
cd `dirname $0`/../solr/3.5.0/example/
java -Djetty.port=8984 -Dsolr.data.dir=./solr/data_shard -jar start.jar
