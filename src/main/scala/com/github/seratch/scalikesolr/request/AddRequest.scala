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
import reflect.BeanProperty

import com.github.seratch.scalikesolr.{SolrDocument, SolrCore}
import collection.JavaConverters._
import util.QueryStringUtil

case class AddRequest(@BeanProperty var core: SolrCore = SolrCore(),
                      var documents: List[SolrDocument] = Nil,
                      @BeanProperty var writerType: WriterType = WriterType.Standard,
                      @BeanProperty var additionalQueryString: String = "") {

  def this() = {
    this (SolrCore(), Nil, WriterType.Standard, "")
  }

  def this(core: SolrCore) = {
    this (core, Nil, WriterType.Standard, "")
  }

  def getDocumentsInJava(): java.util.List[SolrDocument] = documents.asJava

  def setDocumentsInJava(newDocuments: java.util.List[SolrDocument]) = {
    this.documents = newDocuments.asScala.toList
  }

  def toQueryString(): String = {
    val buf = new StringBuilder
    QueryStringUtil.appendIfExists(buf, writerType)
    "?" + buf.toString + additionalQueryString
  }

}