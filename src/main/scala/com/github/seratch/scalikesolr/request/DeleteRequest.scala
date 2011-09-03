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

package com.github.seratch.scalikesolr.request

import common.WriterType
import query.Query
import reflect.BeanProperty

import com.github.seratch.scalikesolr.SolrCore
import collection.JavaConverters._
import util.QueryStringUtil

case class DeleteRequest(@BeanProperty var core: SolrCore = SolrCore(),
                         var uniqueKeysToDelete: List[String] = Nil,
                         var queries: List[Query] = Nil,
                         @BeanProperty var writerType: WriterType = WriterType.Standard,
                         @BeanProperty var additionalQueryString: String = "") {

  def this() = {
    this (SolrCore(), Nil, Nil, WriterType.Standard, "")
  }

  def this(core: SolrCore) = {
    this (core, Nil, Nil, WriterType.Standard, "")
  }

  def getUniqueKeysToDetelteInJava(): java.util.List[String] = uniqueKeysToDelete.asJava

  def setUniqueKeysToDetelteInJava(uniqueKeysToDelete: java.util.List[String]) = {
    this.uniqueKeysToDelete = uniqueKeysToDelete.asScala.toList
  }

  def getQueriesInJava(): java.util.List[Query] = queries.asJava

  def setQueriesInJava(queries: java.util.List[Query]): Unit = {
    this.queries = queries.asScala.toList
  }

  def toQueryString(): String = {
    val buf = new StringBuilder
    QueryStringUtil.appendIfExists(buf, writerType)
    "?" + buf.toString + additionalQueryString
  }

}