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

case class DIHCommandRequest(@BeanProperty val core: SolrCore = SolrCore(),
    @BeanProperty val writerType: WriterType = WriterType(),
    @BeanProperty val command: String) {

  def this(command: String) {
    this(
      core = SolrCore(),
      writerType = WriterType(),
      command = command
    )
  }

  def this(core: SolrCore, command: String) {
    this(
      core = core,
      writerType = WriterType(),
      command = command
    )
  }

  private val extraParams = new collection.mutable.HashMap[String, Any]

  def updateExtraParam(key: String, value: Any) = extraParams.update(key, value)

  def removeExtraParam(key: String) = extraParams.remove(key)

  def toQueryString: String = {
    val buf = new StringBuilder
    buf.append("command=")
    buf.append(command)
    if (extraParams.size > 0) {
      extraParams.keys.foreach {
        key =>
          if (buf.length > 0) buf.append("&")
          buf.append(key)
          buf.append("=")
          buf.append(extraParams.getOrElse(key, ""))
      }
    }
    "?" + buf.toString
  }

}

object DIHCommandRequest {

  val FullImport = new DIHCommandRequest("full-import")

  val DeltaImport = new DIHCommandRequest("delta-import")

}