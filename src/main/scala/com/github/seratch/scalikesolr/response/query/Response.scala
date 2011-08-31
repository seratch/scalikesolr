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

import com.github.seratch.scalikesolr.request.common.WriterType
import xml.{Node, XML}
import com.github.seratch.scalikesolr.util.JSONUtil._
import com.github.seratch.scalikesolr.{SolrDocumentValue, SolrDocument}
import org.apache.solr.common.SolrDocumentList
import reflect.BeanProperty
import org.apache.solr.common.util.NamedList

case class Response(@BeanProperty val numFound: Int,
                    @BeanProperty val start: Int,
                    @BeanProperty val documents: List[SolrDocument]) {

  def getDocumentsInJava(): java.util.List[SolrDocument] = java.util.Arrays.asList(documents.toArray: _*)

}

object Response {

  def extract(writerType: WriterType = WriterType.Standard,
              rawBody: String = "",
              jsonMapFromRawBody: Map[String, Option[Any]],
              rawJavaBin: NamedList[Any] = null): Response = {
    writerType match {
      case WriterType.Standard => {
        val xml = XML.loadString(rawBody)
        val result = (xml \ "result")(0)
        val documents: Seq[SolrDocument] = (result \ "doc") map {
          case doc: Node => new SolrDocument(writerType = writerType, rawBody = doc.toString)
        }
        new Response(
          (result \ "@numFound").text.toInt,
          (result \ "@start").text.toInt,
          documents.toList
        )
      }
      case WriterType.JSON => {
        val response = toMap(jsonMapFromRawBody.get("response"))
        val documents: Seq[SolrDocument] = toList(response.get("docs")) map {
          case doc: Map[String, Option[Any]] => {
            new SolrDocument(
              writerType = writerType,
              map = doc flatMap {
                case (key, value) => Map(key -> new SolrDocumentValue(value.toString))
              }
            )
          }
        }
        new Response(
          normalizeNum(response.get("numFound").getOrElse("0").toString).toInt,
          normalizeNum(response.get("start").getOrElse("0").toString).toInt,
          documents.toList
        )
      }
      case WriterType.JavaBinary => {
        val response = rawJavaBin.get("response").asInstanceOf[SolrDocumentList]
        import collection.JavaConverters._
        new Response(
          response.getNumFound.toInt,
          response.getStart.toInt,
          (response.asScala map {
            case solrjDocument => SolrDocument(
              writerType = WriterType.JavaBinary,
              rawJavaBin = solrjDocument)
          }).toList
        )
      }
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

}

