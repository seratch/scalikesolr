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
import com.github.seratch.scalikesolr.util.JSONUtil

import com.github.seratch.scalikesolr.request.common._
import com.github.seratch.scalikesolr.response.common._
import com.github.seratch.scalikesolr.{SolrDocumentValue, SolrDocument}
import xml.NodeSeq._

case class QueryResponse(@BeanProperty val writerType: WriterType = WriterType.Standard,
                         @BeanProperty val rawBody: String = "") {

  @BeanProperty lazy val responseHeader: ResponseHeader = ResponseParser.getResponseHeader(writerType, rawBody)

  private lazy val jsonMapFromRawBody: Map[String, Option[Any]] = {
    writerType match {
      case WriterType.JSON => JSONUtil.toMap(JSON.parseFull(rawBody))
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
        val response = JSONUtil.toMap(jsonMapFromRawBody.get("response"))
        val numFound = JSONUtil.normalizeNum(response.get("numFound").getOrElse("0").toString).toInt
        val start = JSONUtil.normalizeNum(response.get("start").getOrElse("0").toString).toInt
        val docs = JSONUtil.toList(response.get("docs"))
        val documents: Seq[SolrDocument] = docs map {
          case doc: Map[String, Option[Any]] => {
            val docMap = (doc.keysIterator.map {
              case key => (key, new SolrDocumentValue(doc.getOrElse(key, "").toString))
            }).toMap
            new SolrDocument(writerType = writerType, map = docMap)
          }
        }
        new Response(numFound, start, documents.toList)
      }
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

  @BeanProperty lazy val groups: Groups = {
    writerType match {
      case WriterType.Standard => {
        val xml = XML.loadString(rawBody)
        var matches: Int = 0
        val groups = new collection.mutable.ListBuffer[com.github.seratch.scalikesolr.response.query.Group]
        val groupedCandidates = (xml \ "lst").filter(lst => (lst \ "@name").text == "grouped")
        if (groupedCandidates.size > 0) {
          val grouped = groupedCandidates(0)
          (grouped \ "lst") foreach {
            case lst: Node => {
              matches = (lst \ "int").filter(i => (i \ "@name").text == "matches").apply(0).text.toInt
              val arrCandidates = (lst \ "arr").filter(lst => (lst \ "@name").text == "groups")
              if (arrCandidates.size == 0) {
                // group.format=simple
                val result = (lst \ "result").filter(str => (str \ "@name").text == "doclist").apply(0)
                groups.append(new com.github.seratch.scalikesolr.response.query.Group(
                  groupValue = "",
                  numFound = (result \ "@numFound").text.toInt,
                  start = (result \ "@start").text.toInt,
                  documents = ((result \ "doc") map {
                    case doc: Node => new SolrDocument(writerType = writerType, rawBody = doc.toString)
                  }).toList
                ))
              } else {
                // group.format=grouped
                val arr = (lst \ "arr").filter(lst => (lst \ "@name").text == "groups").apply(0)
                (arr \ "lst") foreach {
                  case lst: Node => {
                    var groupValue = ""
                    val groupValueCandidates = (lst \ "str").filter(str => (str \ "@name").text == "groupValue")
                    if (groupValueCandidates.size > 0) {
                      groupValue = (lst \ "str").filter(str => (str \ "@name").text == "groupValue").apply(0).text
                    }
                    val result = (lst \ "result").filter(str => (str \ "@name").text == "doclist").apply(0)
                    groups.append(new com.github.seratch.scalikesolr.response.query.Group(
                      groupValue = groupValue,
                      numFound = (result \ "@numFound").text.toInt,
                      start = (result \ "@start").text.toInt,
                      documents = ((result \ "doc") map {
                        case doc: Node => new SolrDocument(writerType = writerType, rawBody = doc.toString)
                      }).toList
                    ))
                  }
                  case _ =>
                }
              }
            }
          }
        }
        new Groups(
          matches = matches,
          groups = groups.toList
        )
      }
      case WriterType.JSON => {
        val grouped = JSONUtil.toMap(jsonMapFromRawBody.get("grouped"))
        val groupedSeq = grouped.keys.toSeq
        if (groupedSeq.size == 0) {
          // group.format=simple&group.main=true
          val response = JSONUtil.toMap(jsonMapFromRawBody.get("response"))
          new Groups(
            matches = 0,
            groups = Nil
          )
        } else {
          val fieldName = JSONUtil.toMap(grouped.get(groupedSeq.apply(0)))
          val groupsDocs = JSONUtil.toList(fieldName.get("groups"))
          if (groupsDocs.size == 0) {
            // group.format=simple
            val matches = JSONUtil.normalizeNum(fieldName.getOrElse("matches", "0").toString).toInt
            val doclist = JSONUtil.toMap(fieldName.get("doclist"))
            val documents = JSONUtil.toList(doclist.get("docs")) map {
              case doc => {
                val docMap = (doc.keysIterator.map {
                  case key => (key, new SolrDocumentValue(doc.getOrElse(key, "").toString))
                }).toMap
                new SolrDocument(writerType = writerType, map = docMap)
              }
            }
            new Groups(
              matches = matches,
              groups = List(new com.github.seratch.scalikesolr.response.query.Group(
                groupValue = "",
                numFound = JSONUtil.normalizeNum(doclist.getOrElse("numFound", 0).toString).toInt,
                start = JSONUtil.normalizeNum(doclist.getOrElse("start", 0).toString).toInt,
                documents = documents
              ))
            )
          } else {
            // group.format=grouped
            var matches: Int = 0
            val groups = (groupsDocs.map {
              case groupDoc => {
                matches = JSONUtil.normalizeNum(groupDoc.getOrElse("matches", "0").toString).toInt
                val doclist = JSONUtil.toMap(groupDoc.get("doclist"))
                val documents = JSONUtil.toList(doclist.get("docs")) map {
                  case doc => {
                    val docMap = (doc.keysIterator.map {
                      case key => (key, new SolrDocumentValue(doc.getOrElse(key, "").toString))
                    }).toMap
                    new SolrDocument(writerType = writerType, map = docMap)
                  }
                }
                val groupValue = groupDoc.getOrElse("groupValue", "")
                new com.github.seratch.scalikesolr.response.query.Group(
                  groupValue = if (groupValue == null) "" else groupValue.toString,
                  numFound = JSONUtil.normalizeNum(doclist.getOrElse("numFound", 0).toString).toInt,
                  start = JSONUtil.normalizeNum(doclist.getOrElse("start", 0).toString).toInt,
                  documents = documents
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
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

  @BeanProperty lazy val highlightings: Highlightings = {
    writerType match {
      case WriterType.Standard => {
        val xml = XML.loadString(rawBody)
        val hashMap = new collection.mutable.HashMap[String, SolrDocument]
        val hlCandidates = (xml \ "lst").filter(lst => (lst \ "@name").text == "highlighting")
        if (hlCandidates.size > 0) {
          val hl = hlCandidates(0)
          (hl \ "lst") foreach {
            case lst: Node => {
              val element = new collection.mutable.HashMap[String, SolrDocumentValue]
              (lst \ "arr") foreach {
                arr => element.update((arr \ "@name").text, new SolrDocumentValue(arr.child.text))
              }
              val doc = new SolrDocument(map = element.toMap)
              hashMap.update((lst \ "@name").text, doc)
            }
          }
        }
        new Highlightings(
          highlightings = hashMap.toMap
        )
      }
      case WriterType.JSON => {
        val highlighting = JSONUtil.toMap(jsonMapFromRawBody.get("highlighting"))
        val highlightings = (highlighting.keysIterator.map {
          case key => {
            val doc = JSONUtil.toMap(highlighting.get(key))
            val docHashMap = new collection.mutable.HashMap[String, SolrDocumentValue]
            doc.keys.foreach {
              case docKey => {
                docHashMap.update(docKey, new SolrDocumentValue(doc.getOrElse(docKey, "").toString))
              }
            }
            (key, new SolrDocument(
              writerType = WriterType.JSON,
              map = docHashMap.toMap
            ))
          }
        }).toMap
        new Highlightings(
          highlightings = highlightings
        )
      }
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

  @BeanProperty lazy val moreLikeThis: MoreLikeThis = {
    writerType match {
      case WriterType.Standard => {
        val xml = XML.loadString(rawBody)
        var numFound: Int = 0
        var start: Int = 0
        val idAndRecommendations = new collection.mutable.HashMap[String, List[SolrDocument]]
        val mltCandidates = (xml \ "lst").filter(lst => (lst \ "@name").text == "moreLikeThis")
        if (mltCandidates.size > 0) {
          val mlt = mltCandidates(0)
          (mlt \ "result") foreach {
            case result: Node => {
              val solrDocs = (result \ "doc") map {
                doc => {
                  val docMap = new collection.mutable.HashMap[String, SolrDocumentValue]
                  doc.child foreach {
                    case field => docMap.update((field \ "@name").text, new SolrDocumentValue(field.text))
                  }
                  new SolrDocument(map = docMap.toMap)
                }
              }
              idAndRecommendations.update((result \ "@name").text, solrDocs.toList)
              numFound = ((result \ "@numFound").text).toInt
              start = ((result \ "@start").text).toInt
            }
          }
        }
        new MoreLikeThis(
          numFound = numFound,
          start = start,
          idAndRecommendations = idAndRecommendations.toMap
        )
      }
      case WriterType.JSON => {
        val moreLikeThis = JSONUtil.toMap(jsonMapFromRawBody.get("moreLikeThis"))
        var numFound: Int = 0
        var start: Int = 0
        val idAndRecommendations = new collection.mutable.HashMap[String, List[SolrDocument]]
        moreLikeThis.keysIterator.foreach {
          case id => {
            val eachMlt = JSONUtil.toMap(moreLikeThis.get(id))
            val docs = JSONUtil.toList(eachMlt.get("docs"))
            val solrDocs = docs map {
              case doc => {
                val docMap = new collection.mutable.HashMap[String, SolrDocumentValue]
                doc.keys.foreach {
                  case field => docMap.update(field, new SolrDocumentValue(doc.getOrElse(field, "").toString))
                }
                new SolrDocument(writerType = WriterType.JSON, map = docMap.toMap)
              }
            }
            idAndRecommendations.update(id, solrDocs)
            numFound = JSONUtil.normalizeNum(eachMlt.get("numFound").getOrElse(0).toString).toInt
            start = JSONUtil.normalizeNum(eachMlt.get("start").getOrElse(0).toString).toInt
          }
        }
        new MoreLikeThis(
          numFound = numFound,
          start = start,
          idAndRecommendations = idAndRecommendations.toMap
        )
      }
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

  @BeanProperty lazy val facet: Facet = {
    writerType match {
      case WriterType.Standard => {
        val xml = XML.loadString(rawBody)
        val facetQueriesMap = new collection.mutable.HashMap[String, SolrDocument]
        val facetFieldsMap = new collection.mutable.HashMap[String, SolrDocument]
        val facetDatesMap = new collection.mutable.HashMap[String, SolrDocument]
        val facetRangesMap = new collection.mutable.HashMap[String, SolrDocument]
        val facetCountsCandidates = (xml \ "lst").filter(lst => (lst \ "@name").text == "facet_counts")
        if (facetCountsCandidates.size > 0) {
          val facetCounts = facetCountsCandidates(0)
          facetCounts.child foreach {
            case node: Node => {
              (node \ "@name").text match {
                case "facet_queries" => {
                  (node \ "lst") foreach {
                    query => {
                      val element = new collection.mutable.HashMap[String, SolrDocumentValue]
                      query.child foreach (value => element.update((value \ "@name").text, SolrDocumentValue(value.text)))
                      facetQueriesMap.update((query \ "@name").text, SolrDocument(map = element.toMap))
                    }
                  }
                }
                case "facet_fields" => {
                  node.child foreach {
                    field => {
                      val element = new collection.mutable.HashMap[String, SolrDocumentValue]
                      field.child foreach (value => element.update((value \ "@name").text, SolrDocumentValue(value.text)))
                      facetFieldsMap.update((field \ "@name").text, SolrDocument(map = element.toMap))
                    }
                  }
                }
                case "facet_dates" => {
                  (node \ "lst") foreach {
                    date => {
                      val element = new collection.mutable.HashMap[String, SolrDocumentValue]
                      date.child foreach (value => element.update((value \ "@name").text, SolrDocumentValue(value.text)))
                      facetDatesMap.update((date \ "@name").text, SolrDocument(map = element.toMap))
                    }
                  }
                }
                case "facet_ranges" => {
                  (node \ "lst") foreach {
                    range => {
                      val element = new collection.mutable.HashMap[String, SolrDocumentValue]
                      range.child foreach (value => element.update((value \ "@name").text, SolrDocumentValue(value.text)))
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
        new Facet(
          facetQueries = facetQueriesMap.toMap,
          facetFields = facetFieldsMap.toMap,
          facetDates = facetDatesMap.toMap,
          facetRanges = facetRangesMap.toMap
        )
      }
      case WriterType.JSON => {

        def toMap(facets: List[Any]): Map[String, SolrDocumentValue] = {
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

        val facetCounts = JSONUtil.toMap(jsonMapFromRawBody.get("facet_counts"))
        facetCounts.keys.foreach {
          case key if key == "facet_queries" => {
            val doc = JSONUtil.toMap(facetCounts.get(key))
            doc.keys.foreach {
              case docKey => {
                facetQueriesMap.update(docKey, new SolrDocument(
                  writerType = WriterType.JSON,
                  map = toMap(doc.getOrElse(docKey, Nil).asInstanceOf[List[Any]])
                ))
              }
            }
          }
          case key if key == "facet_fields" => {
            val doc = JSONUtil.toMap(facetCounts.get(key))
            doc.keys.foreach {
              case docKey => {
                facetFieldsMap.update(docKey, new SolrDocument(
                  writerType = WriterType.JSON,
                  map = toMap(doc.getOrElse(docKey, Nil).asInstanceOf[List[Any]])
                ))
              }
            }
          }
          case key if key == "facet_dates" => {
            val doc = JSONUtil.toMap(facetCounts.get(key))
            doc.keys.foreach {
              case docKey => {
                facetDatesMap.update(docKey, new SolrDocument(
                  writerType = WriterType.JSON,
                  map = toMap(doc.getOrElse(docKey, Nil).asInstanceOf[List[Any]])
                ))
              }
            }
          }
          case key if key == "facet_ranges" => {
            val doc = JSONUtil.toMap(facetCounts.get(key))
            doc.keys.foreach {
              case docKey => {
                facetRangesMap.update(docKey, new SolrDocument(
                  writerType = WriterType.JSON,
                  map = toMap(doc.getOrElse(docKey, Nil).asInstanceOf[List[Any]])
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
