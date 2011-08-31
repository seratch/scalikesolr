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
import xml.{Node, XML}
import com.github.seratch.scalikesolr.SolrDocumentValue._
import com.github.seratch.scalikesolr.{SolrDocumentValue, SolrDocument}
import com.github.seratch.scalikesolr.util.JSONUtil._

case class Facet(@BeanProperty val facetQueries: Map[String, SolrDocument],
                 @BeanProperty val facetFields: Map[String, SolrDocument],
                 @deprecated @BeanProperty val facetDates: Map[String, SolrDocument],
                 @BeanProperty val facetRanges: Map[String, SolrDocument]) {

  def getFromFacetFields(name: String): SolrDocument = facetFields.getOrElse(name, null)

  @deprecated def getFromFacetDates(date: String): SolrDocument = facetDates.getOrElse(date, null)

  def getFromFacetRanges(range: String): SolrDocument = facetRanges.getOrElse(range, null)

}

object Facet {

  def extract(writerType: WriterType = WriterType.Standard,
              rawBody: String = "",
              jsonMapFromRawBody: Map[String, Option[Any]],
              rawJavaBin: NamedList[Any] = null): Facet = {
    writerType match {
      case WriterType.Standard => {

        val facetQueriesMap = new collection.mutable.HashMap[String, SolrDocument]
        val facetFieldsMap = new collection.mutable.HashMap[String, SolrDocument]
        val facetDatesMap = new collection.mutable.HashMap[String, SolrDocument]
        val facetRangesMap = new collection.mutable.HashMap[String, SolrDocument]

        val xml = XML.loadString(rawBody)
        val facetCountsList = (xml \ "lst").filter(lst => (lst \ "@name").text == "facet_counts")
        facetCountsList.size match {
          case 0 =>
          case _ => {
            facetCountsList.head.child foreach {
              case node: Node => {
                (node \ "@name").text match {
                  case "facet_queries" => {
                    (node \ "lst") foreach {
                      query => {
                        val element = query.child map (value => ((value \ "@name").text, SolrDocumentValue(value.text)))
                        facetQueriesMap.update((query \ "@name").text, SolrDocument(map = element.toMap))
                      }
                    }
                  }
                  case "facet_fields" => {
                    node.child foreach {
                      field => {
                        val element = field.child map (value => ((value \ "@name").text, SolrDocumentValue(value.text)))
                        facetFieldsMap.update((field \ "@name").text, SolrDocument(map = element.toMap))
                      }
                    }
                  }
                  case "facet_dates" => {
                    (node \ "lst") foreach {
                      date => {
                        val element = date.child map (value => ((value \ "@name").text, SolrDocumentValue(value.text)))
                        facetDatesMap.update((date \ "@name").text, SolrDocument(map = element.toMap))
                      }
                    }
                  }
                  case "facet_ranges" => {
                    (node \ "lst") foreach {
                      range => {
                        val element = range.child map (value => ((value \ "@name").text, SolrDocumentValue(value.text)))
                        facetRangesMap.update((range \ "@name").text, SolrDocument(map = element.toMap))
                      }
                    }
                  }
                  case _ =>
                }
              }
              case _ =>
            }
          }
        }
        new Facet(
          facetQueries = facetQueriesMap.toMap,
          facetFields = facetFieldsMap.toMap,
          facetDates = facetDatesMap.toMap,
          facetRanges = facetRangesMap.toMap
        )
      }
      case WriterType.JSON => {

        def fromListToMap(facets: List[Any]): Map[String, SolrDocumentValue] = {
          def parseFacetsToMap(facets: List[Any], docHashMap: collection.mutable.HashMap[String, SolrDocumentValue])
          : collection.mutable.HashMap[String, SolrDocumentValue] = {
            facets.size match {
              case 0 => docHashMap
              case _ => {
                val kv = facets.take(2)
                docHashMap.update(kv(0).toString, new SolrDocumentValue(kv(1).toString))
                parseFacetsToMap(facets.drop(2), docHashMap)
              }
            }
          }
          parseFacetsToMap(facets, new collection.mutable.HashMap[String, SolrDocumentValue]).toMap
        }

        val facetQueriesMap = new collection.mutable.HashMap[String, SolrDocument]
        val facetFieldsMap = new collection.mutable.HashMap[String, SolrDocument]
        val facetDatesMap = new collection.mutable.HashMap[String, SolrDocument]
        val facetRangesMap = new collection.mutable.HashMap[String, SolrDocument]

        val facetCounts = toMap(jsonMapFromRawBody.get("facet_counts"))
        facetCounts.keys.foreach {
          case key if key == "facet_queries" => {
            val doc = toMap(facetCounts.get(key))
            doc.keys.foreach {
              case docKey => {
                facetQueriesMap.update(docKey, new SolrDocument(
                  writerType = WriterType.JSON,
                  map = fromListToMap(doc.getOrElse(docKey, Nil).asInstanceOf[List[Any]])
                ))
              }
            }
          }
          case key if key == "facet_fields" => {
            val doc = toMap(facetCounts.get(key))
            doc.keys.foreach {
              case docKey => {
                facetFieldsMap.update(docKey, new SolrDocument(
                  writerType = WriterType.JSON,
                  map = fromListToMap(doc.getOrElse(docKey, Nil).asInstanceOf[List[Any]])
                ))
              }
            }
          }
          case key if key == "facet_dates" => {
            val doc = toMap(facetCounts.get(key))
            doc.keys.foreach {
              case docKey => {
                facetDatesMap.update(docKey, new SolrDocument(
                  writerType = WriterType.JSON,
                  map = fromListToMap(doc.getOrElse(docKey, Nil).asInstanceOf[List[Any]])
                ))
              }
            }
          }
          case key if key == "facet_ranges" => {
            val doc = toMap(facetCounts.get(key))
            doc.keys.foreach {
              case docKey => {
                facetRangesMap.update(docKey, new SolrDocument(
                  writerType = WriterType.JSON,
                  map = fromListToMap(doc.getOrElse(docKey, Nil).asInstanceOf[List[Any]])
                ))
              }
            }
          }
        }
        new Facet(
          facetQueries = facetQueriesMap.toMap,
          facetFields = facetFieldsMap.toMap,
          facetDates = facetDatesMap.toMap,
          facetRanges = facetRangesMap.toMap
        )
      }
      case WriterType.JavaBinary => {
        // TODO
        throw new UnsupportedOperationException("currently not supported.")
      }
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

}
