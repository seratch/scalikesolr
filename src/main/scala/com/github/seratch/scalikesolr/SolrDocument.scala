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

package com.github.seratch.scalikesolr

import reflect.BeanProperty
import util.{TypeBinder, JSONUtil}
import xml.{Node, XML}
import scala.util.parsing.json.JSON

case class SolrDocument(@BeanProperty val writerType: WriterType = WriterType.Standard,
                        @BeanProperty val rawBody: String = "",
                        @BeanProperty val map: Map[String, SolrDocumentValue] = Map()) {

  def this(writerType: WriterType, rawBody: String) = {
    this (writerType = writerType, rawBody = rawBody, map = Map())
  }

  def this(writerType: WriterType, map: Map[String, SolrDocumentValue]) = {
    this (writerType = writerType, rawBody = "", map = map)
  }

  private lazy val jsonMapFromRawBody: Map[String, Option[Any]] = {
    writerType match {
      case WriterType.JSON => JSONUtil.toMap(JSON.parseFull(rawBody))
      case _ => Map()
    }
  }

  private lazy val document: Map[String, SolrDocumentValue] = {
    if (map != null && map.size > 0) {
      map
    } else {
      (writerType match {
        case WriterType.Standard => {
          XML.loadString(rawBody).child map {
            case elem: Node => ((elem \ "@name").toString, new SolrDocumentValue(elem.text))
          }
        }
        case WriterType.JSON => {
          jsonMapFromRawBody.keys map {
            case key => {
              val value = JSONUtil.normalizeNum(jsonMapFromRawBody.get(key).getOrElse("").toString)
              (key, new SolrDocumentValue(value))
            }
          }
        }
        case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
      }).toMap
    }
  }

  def keys(): List[String] = document.keys.toList

  def get(key: String): SolrDocumentValue = document.getOrElse(key, SolrDocumentValue(""))

  def bind[T](clazz: Class[T]): T = TypeBinder.bind(this, clazz)

  def bindInJava[T](clazz: Class[T]): T = TypeBinder.bindInJava(this, clazz)

}
