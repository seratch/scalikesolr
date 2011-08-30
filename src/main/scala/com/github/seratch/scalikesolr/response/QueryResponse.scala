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

package com.github.seratch.scalikesolr.response

import parser.ResponseParser
import query._
import reflect.BeanProperty

import xml._

import util.parsing.json.JSON
import com.github.seratch.scalikesolr.util.JSONUtil._

import com.github.seratch.scalikesolr.request.common._
import com.github.seratch.scalikesolr.response.common._
import com.github.seratch.scalikesolr.{SolrDocumentValue, SolrDocument}
import xml.NodeSeq._
import collection.immutable.Map

case class QueryResponse(@BeanProperty val writerType: WriterType = WriterType.Standard,
                         @BeanProperty val rawBody: String = "") {

  @BeanProperty lazy val responseHeader: ResponseHeader = ResponseParser.getResponseHeader(writerType, rawBody)

  private lazy val jsonMapFromRawBody: Map[String, Option[Any]] = {
    writerType match {
      case WriterType.JSON => toMap(JSON.parseFull(rawBody))
      case _ => Map()
    }
  }

  @BeanProperty lazy val response: Response = {
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
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

  @BeanProperty lazy val groups: Groups = {
    writerType match {
      case WriterType.Standard => {
        var matches: Int = 0
        val empty: List[com.github.seratch.scalikesolr.response.query.Group] = Nil
        val xml = XML.loadString(rawBody)
        val groupedList = (xml \ "lst").filter(lst => (lst \ "@name").text == "grouped")
        val groups = groupedList.size match {
          case 0 => empty
          case _ => {
            (groupedList.head \ "lst") flatMap {
              case lst: Node => {
                matches = (lst \ "int").filter(i => (i \ "@name").text == "matches").apply(0).text.toInt
                val arrList = (lst \ "arr").filter(lst => (lst \ "@name").text == "groups")
                arrList.size match {
                  case 0 => {
                    // group.format=simple
                    val result = (lst \ "result").filter(str => (str \ "@name").text == "doclist").apply(0)
                    List(
                      new com.github.seratch.scalikesolr.response.query.Group(
                        groupValue = "",
                        numFound = (result \ "@numFound").text.toInt,
                        start = (result \ "@start").text.toInt,
                        documents = ((result \ "doc") map {
                          case doc: Node => new SolrDocument(writerType = writerType, rawBody = doc.toString)
                        }).toList
                      )
                    )
                  }
                  case _ => {
                    // group.format=grouped
                    val arr = (lst \ "arr").filter(lst => (lst \ "@name").text == "groups").apply(0)
                    (arr \ "lst") flatMap {
                      case lst: Node => {
                        val groupValueList = (lst \ "str").filter(str => (str \ "@name").text == "groupValue")
                        val groupValue = groupValueList.size match {
                          case 0 => ""
                          case _ => (lst \ "str").filter(str => (str \ "@name").text == "groupValue").apply(0).text
                        }
                        val result = (lst \ "result").filter(str => (str \ "@name").text == "doclist").apply(0)
                        List(
                          new com.github.seratch.scalikesolr.response.query.Group(
                            groupValue = groupValue,
                            numFound = (result \ "@numFound").text.toInt,
                            start = (result \ "@start").text.toInt,
                            documents = ((result \ "doc") map {
                              case doc: Node => new SolrDocument(writerType = writerType, rawBody = doc.toString)
                            }).toList
                          )
                        )
                      }
                      case _ => empty
                    }
                  }
                }
              }
              case _ => empty
            }
          }
        }
        new Groups(
          matches = matches,
          groups = groups.toList
        )
      }
      case WriterType.JSON => {
        val grouped = toMap(jsonMapFromRawBody.get("grouped"))
        grouped.keys.size match {
          case 0 => {
            // group.format=simple&group.main=true
            new Groups(
              matches = 0,
              groups = Nil
            )
          }
          case _ => {
            val fieldName = toMap(grouped.get(grouped.keys.toSeq.head))
            val groupsList = toList(fieldName.get("groups"))
            groupsList.size match {
              case 0 => {
                // group.format=simple
                val doclist = toMap(fieldName.get("doclist"))
                new Groups(
                  matches = normalizeNum(fieldName.getOrElse("matches", "0").toString).toInt,
                  groups = List(new com.github.seratch.scalikesolr.response.query.Group(
                    groupValue = "",
                    numFound = normalizeNum(doclist.getOrElse("numFound", 0).toString).toInt,
                    start = normalizeNum(doclist.getOrElse("start", 0).toString).toInt,
                    documents = toList(doclist.get("docs")) map {
                      case doc => {
                        new SolrDocument(
                          writerType = writerType,
                          map = doc flatMap {
                            case (key, value) => Map(key -> new SolrDocumentValue(value.toString))
                          }
                        )
                      }
                    }
                  ))
                )
              }
              case _ => {
                // group.format=grouped
                var matches: Int = 0
                val groups = (groupsList.map {
                  case groupDoc => {
                    matches = normalizeNum(groupDoc.getOrElse("matches", "0").toString).toInt
                    val doclist = toMap(groupDoc.get("doclist"))
                    val groupValue = groupDoc.getOrElse("groupValue", "")
                    new com.github.seratch.scalikesolr.response.query.Group(
                      groupValue = if (groupValue == null) "" else groupValue.toString,
                      numFound = normalizeNum(doclist.getOrElse("numFound", 0).toString).toInt,
                      start = normalizeNum(doclist.getOrElse("start", 0).toString).toInt,
                      documents = toList(doclist.get("docs")) map {
                        case doc => {
                          new SolrDocument(writerType = writerType, map = doc map {
                            case (key, value) => (key, new SolrDocumentValue(value.toString))
                          })
                        }
                      }
                    )
                  }
                }).toList
                new Groups(
                  matches = matches,
                  groups = groups
                )
              }
            }
          }
        }
      }
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

  @BeanProperty lazy val highlightings: Highlightings = {
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
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

  @BeanProperty lazy val moreLikeThis: MoreLikeThis = {
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
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

  @BeanProperty lazy val facet: Facet = {
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
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

}
