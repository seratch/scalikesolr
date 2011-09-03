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

import com.github.seratch.scalikesolr.SolrCore
import util.QueryStringUtil

case class PingRequest(@BeanProperty var core: SolrCore = SolrCore(),
                       @BeanProperty var writerType: WriterType = WriterType()) {

  def this() = {
    this (SolrCore(), WriterType())
  }

  def this(core: SolrCore) = {
    this (core, WriterType())
  }

  def queryString(): String = {
    val buf = new StringBuilder
    QueryStringUtil.appendIfExists(buf, this.writerType)
    "?" + buf.toString.replaceAll(" ", "%20")
  }

}