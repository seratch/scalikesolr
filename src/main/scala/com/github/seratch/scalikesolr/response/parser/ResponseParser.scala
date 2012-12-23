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

package com.github.seratch.scalikesolr.response.parser

import com.github.seratch.scalikesolr.request.common.WriterType
import com.github.seratch.scalikesolr.response.common.ResponseHeader
import com.github.seratch.scalikesolr.{ SolrDocument, SolrDocumentValue }
import scala.xml.XML
import org.apache.solr.common.util.{ SimpleOrderedMap, NamedList }

object ResponseParser {

  def getResponseHeader(writerType: WriterType,
    rawBody: String,
    rawJavaBin: NamedList[Any] = null): ResponseHeader = {
    writerType match {
      case WriterType.Standard => {
        val xml = XML.loadString(rawBody)
        val intItems = xml \ "lst" \ "int"
        val paramsList = (xml \ "lst" \ "list").filter(item => (item \ "@name").text == "params")
        paramsList.size match {
          case 0 =>
            new ResponseHeader(
              intItems.filter(item => (item \ "@name").text == "status").head.text.toInt,
              intItems.filter(item => (item \ "@name").text == "QTime").head.text.toInt
            )
          case _ =>
            val params = paramsList(0)
            new ResponseHeader(
              intItems.filter(item => (item \ "@name").text == "status").head.text.toInt,
              intItems.filter(item => (item \ "@name").text == "QTime").head.text.toInt,
              SolrDocument(writerType = writerType, rawBody = params.toString)
            )
        }
      }
      case WriterType.JavaBinary => {
        val responseHeader = rawJavaBin.get("responseHeader").asInstanceOf[NamedList[Any]]
        import collection.JavaConverters._
        val docMap = responseHeader.get("params").asInstanceOf[SimpleOrderedMap[Any]].asScala.map {
          case e: java.util.Map.Entry[_, _] => (e.getKey.toString, new SolrDocumentValue(e.getValue.toString))
        }.toMap
        new ResponseHeader(
          responseHeader.get("status").toString.toInt,
          responseHeader.get("QTime").toString.toInt,
          new SolrDocument(writerType = writerType, map = docMap)
        )
      }
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

}
