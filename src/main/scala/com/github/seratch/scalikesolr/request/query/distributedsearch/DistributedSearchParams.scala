/*
 * Copyright 2011 Kazuhiro Sera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.github.seratch.scalikesolr.request.query.distributedsearch

import java.lang.StringBuilder

case class DistributedSearchParams(var shards: List[String] = Nil) {

  def this() {
    this(Nil)
  }

  import collection.JavaConverters._

  def setShardsInJava(shardsInJava: java.util.List[String]): Unit = {
    shards = shardsInJava.asScala.toList
  }

  def getShardsInJava(): java.util.List[String] = shards.asJava

  override def toString(): String = {
    val buf = new StringBuilder
    shards match {
      case Nil =>
      case _ => {
        buf.append("shards=")
        shards foreach {
          case shard => {
            buf.append(shard)
            buf.append(",")
          }
        }
      }
    }
    buf.toString.replaceFirst(",$", "")
  }

  def toQueryString(): String = toString()

}
