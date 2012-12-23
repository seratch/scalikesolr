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

import scala.beans.BeanProperty
import scala.xml.{ Node, XML }
import util.TypeBinder

case class SolrDocument(@BeanProperty val writerType: WriterType = WriterType.Standard,
    @BeanProperty val rawBody: String = "",
    @BeanProperty val rawJavabin: SolrjSolrDocument = new SolrjSolrDocument,
    @BeanProperty val map: Map[String, SolrDocumentValue] = Map()) {

  private lazy val document: Map[String, SolrDocumentValue] = {
    if (map != null && map.size > 0) {
      map
    } else {
      import collection.JavaConverters._
      (writerType match {
        case WriterType.Standard =>
          XML.loadString(rawBody).child map {
            case elem: Node => ((elem \ "@name").toString, new SolrDocumentValue(elem.text))
          }
        case WriterType.JavaBinary =>
          rawJavabin.getFieldNames.asScala map (e => (e.toString -> new SolrDocumentValue(rawJavabin.get(e).toString)))
        case other =>
          throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
      }).toMap
    }
  }

  def keys(): List[String] = document.keys.toList filter (k => k != null && !k.isEmpty)

  def keysInJava(): java.util.List[String] = java.util.Arrays.asList(keys().toArray: _*)

  def get(key: String): SolrDocumentValue = document.getOrElse(key, SolrDocumentValue(""))

  def bind[T](clazz: Class[T]): T = TypeBinder.bind(this, clazz)

  def bindInJava[T](clazz: Class[T]): T = TypeBinder.bindInJava(this, clazz)

}
