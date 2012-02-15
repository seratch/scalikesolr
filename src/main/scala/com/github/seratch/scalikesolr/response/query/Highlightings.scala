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

package com.github.seratch.scalikesolr.response.query

import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.WriterType
import org.apache.solr.common.util.NamedList
import xml.{ Node, XML }
import com.github.seratch.scalikesolr.{ SolrDocumentValue, SolrDocument }
import com.github.seratch.scalikesolr.util.JSONUtil._

case class Highlightings(@BeanProperty val highlightings: Map[String, SolrDocument]) {

  def keys(): List[String] = highlightings.keys.toList

  def keysInJava(): java.util.List[String] = java.util.Arrays.asList(highlightings.keys.toArray: _*)

  def get(name: String): SolrDocument = highlightings.getOrElse(name, new SolrDocument())

  def size(): Int = highlightings.size

}

object Highlightings {

  def extract(writerType: WriterType = WriterType.Standard,
    rawBody: String = "",
    jsonMapFromRawBody: Map[String, Option[Any]],
    rawJavaBin: NamedList[Any] = null): Highlightings = {

    writerType match {
      case WriterType.Standard => {
        val xml = XML.loadString(rawBody)
        val hlList = (xml \ "lst").filter(lst => (lst \ "@name").text == "highlighting")
        new Highlightings(
          highlightings = hlList.size match {
            case 0 => Map()
            case _ => {
              val hl = hlList(0)
              ((hl \ "lst") map {
                case lst: Node => {
                  val element = (lst \ "arr") map {
                    arr => ((arr \ "@name").text, new SolrDocumentValue(arr.child.text))
                  }
                  ((lst \ "@name").text, new SolrDocument(map = element.toMap))
                }
              }).toMap
            }
          })
      }
      case WriterType.JSON => {
        val highlighting = toMap(jsonMapFromRawBody.get("highlighting"))
        new Highlightings(
          highlightings = (highlighting.keys.map {
            case key => {
              val docMap = toMap(highlighting.get(key))
              (key, new SolrDocument(
                writerType = WriterType.JSON,
                map = (docMap.keys.map {
                  case docKey => (docKey, new SolrDocumentValue(docMap.getOrElse(docKey, "").toString))
                }).toMap
              ))
            }
          }).toMap
        )
      }
      case WriterType.JavaBinary => {
        val highlighting = rawJavaBin.get("highlighting").asInstanceOf[NamedList[Any]]
        import collection.JavaConverters._
        new Highlightings(
          highlightings = (highlighting.iterator().asScala map {
            case e: java.util.Map.Entry[_, _] => {
              val element = e.getValue.asInstanceOf[NamedList[Any]]
              (e.getKey.toString -> new SolrDocument(
                writerType = WriterType.JavaBinary,
                map = (element.iterator.asScala map {
                  case eachInValue: java.util.Map.Entry[_, _] => {
                    val value = eachInValue.getValue.toString.replaceFirst("^\\[", "").replaceFirst("\\]$", "")
                    (eachInValue.getKey.toString, new SolrDocumentValue(value))
                  }
                }).toMap))
            }
          }).toMap
        )
      }
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

}
