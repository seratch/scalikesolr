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

import collection.JavaConverters._
import com.github.seratch.scalikesolr.request.common.WriterType
import org.apache.solr.common.util.NamedList
import xml.{Node, XML}
import com.github.seratch.scalikesolr.{SolrDocumentValue, SolrDocument}
import com.github.seratch.scalikesolr.util.JSONUtil._
import scala.Option._
import org.apache.solr.common.SolrDocumentList
import com.github.seratch.scalikesolr.SolrjSolrDocument

case class MoreLikeThis(@BeanProperty val numFound: Int = 0,
                        @BeanProperty val start: Int = 0,
                        @BeanProperty val idAndRecommendations: Map[String, List[SolrDocument]]) {

  def getList(name: String): List[SolrDocument] = idAndRecommendations.getOrElse(name, Nil)

  def getListInJava(name: String): java.util.List[SolrDocument] = getList(name).asJava

}

object MoreLikeThis {

  def extract(writerType: WriterType = WriterType.Standard,
              rawBody: String = "",
              jsonMapFromRawBody: Map[String, Option[Any]],
              rawJavabin: NamedList[Any] = null): MoreLikeThis = {

    writerType match {
      case WriterType.Standard => {
        var numFound: Int = 0
        var start: Int = 0
        val xml = XML.loadString(rawBody)
        val mltList = (xml \ "lst").filter(lst => (lst \ "@name").text == "moreLikeThis")
        val idAndRecommendations: Map[String, List[SolrDocument]] = mltList.size match {
          case size if size > 0 => {
            ((mltList.head \ "result") flatMap {
              case result: Node => {
                numFound = ((result \ "@numFound").text).toInt
                start = ((result \ "@start").text).toInt
                val solrDocs = (result \ "doc") map {
                  doc => {
                    new SolrDocument(map = (doc.child map {
                      case field => ((field \ "@name").text, new SolrDocumentValue(field.text))
                    }).toMap)
                  }
                }
                Map((result \ "@name").text -> solrDocs.toList)
              }
            }).toMap
          }
          case _ => Map()
        }
        new MoreLikeThis(
          numFound = numFound,
          start = start,
          idAndRecommendations = idAndRecommendations
        )
      }
      case WriterType.JSON => {
        var numFound: Int = 0
        var start: Int = 0
        val moreLikeThis = toMap(jsonMapFromRawBody.get("moreLikeThis"))
        val idAndRecommendations: Map[String, List[SolrDocument]] = (moreLikeThis.keys flatMap {
          case id: String => {
            val eachMlt = toMap(moreLikeThis.get(id))
            numFound = normalizeNum(eachMlt.get("numFound").getOrElse(0).toString).toInt
            start = normalizeNum(eachMlt.get("start").getOrElse(0).toString).toInt
            Map(id -> toList(eachMlt.get("docs")).map {
              case doc => {
                new SolrDocument(writerType = WriterType.JSON, map = (doc.keys.map {
                  case field => (field, new SolrDocumentValue(doc.getOrElse(field, "").toString))
                }).toMap)
              }
            })
          }
          case _ => None
        }).toMap
        new MoreLikeThis(
          numFound = numFound,
          start = start,
          idAndRecommendations = idAndRecommendations
        )
      }
      case WriterType.JavaBinary => {
        var numFound: Int = 0
        var start: Int = 0
        val moreLikeThis = rawJavabin.get("moreLikeThis").asInstanceOf[NamedList[Any]]
        val idAndRecommendations: Map[String, List[SolrDocument]] = (moreLikeThis.asScala flatMap {
          case e: java.util.Map.Entry[String, Any] => {
            val id = e.getKey
            val mlt = e.getValue.asInstanceOf[SolrDocumentList]
            numFound = mlt.getNumFound.toInt
            start = mlt.getStart.toInt
            val recommendations: List[SolrDocument] = (mlt.asScala map {
              case doc: SolrjSolrDocument => {
                new SolrDocument(
                  writerType = WriterType.JavaBinary,
                  map = (doc.keySet.asScala map {
                    case key => {
                      (key.toString -> new SolrDocumentValue(doc.get(key).toString))
                    }
                  }).toMap
                )
              }
            }).toList
            Map(id -> recommendations)
          }
          case _ => None
        }).toMap
        new MoreLikeThis(
          numFound = numFound,
          start = start,
          idAndRecommendations = idAndRecommendations
        )
      }
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

}
