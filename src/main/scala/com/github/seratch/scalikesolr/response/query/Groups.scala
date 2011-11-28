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

import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.WriterType
import xml.{Node, XML}
import com.github.seratch.scalikesolr.util.JSONUtil._
import com.github.seratch.scalikesolr.{SolrDocumentValue, SolrDocument, SolrjSolrDocument}
import org.apache.solr.common.util.{NamedList, SimpleOrderedMap}
import org.apache.solr.common.SolrDocumentList

case class Groups(@BeanProperty val matches: Int = 0,
                  @BeanProperty val groups: List[Group])

case class Group(@BeanProperty val numFound: Int = 0,
                 @BeanProperty val start: Int = 0,
                 @BeanProperty val groupValue: String,
                 @BeanProperty val documents: List[SolrDocument])

object Groups {

  def extract(writerType: WriterType = WriterType.Standard,
              rawBody: String = "",
              jsonMapFromRawBody: Map[String, Option[Any]],
              rawJavaBin: NamedList[Any] = null): Groups = {

    writerType match {
      case WriterType.Standard => {
        var matches: Int = 0
        val empty: List[Group] = Nil
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
                      new Group(
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
                          new Group(
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
                  groups = List(new Group(
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
                    new Group(
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
      case WriterType.JavaBinary => {

        type MapEntry = java.util.Map.Entry[_, _]
        import collection.JavaConverters._

        var matches: Int = 0
        var groups: List[Group] = Nil

        val grouped = rawJavaBin.get("grouped").asInstanceOf[SimpleOrderedMap[Any]]
        // since Solr 3.5: when specifying "main=true",
        // the "grouped" element does not exist and it causes NPE.
        if (grouped != null) {
          grouped.asScala foreach {
            case e: MapEntry => {
              val groupedValue = e.getValue.asInstanceOf[NamedList[Any]]
              groupedValue.asScala foreach {
                case e: MapEntry if e.getKey == "matches" => {
                  matches = e.getValue.toString.toInt
                }
                case e: MapEntry if e.getKey == "doclist" => {
                  // group.format=simple
                  val doclist = e.getValue.asInstanceOf[SolrDocumentList]
                  groups = List(
                    new Group(
                      groupValue = null,
                      numFound = doclist.getNumFound.toInt,
                      start = doclist.getStart.toInt,
                      documents = (doclist.asScala map {
                        case doc: SolrjSolrDocument => {
                          new SolrDocument(
                            writerType = writerType,
                            map = (doc.keySet.asScala.map {
                              case key => (key, new SolrDocumentValue(doc.get(key).toString))
                            }).toMap
                          )
                        }
                      }).toList
                    )
                  )
                }
                case e: MapEntry if e.getKey == "groups" => {
                  // group.format=grouped
                  val groupsList = e.getValue.asInstanceOf[java.util.List[NamedList[Any]]]
                  groups = (groupsList.asScala map {
                    case g: NamedList[_] => {
                      val groupValue = g.get("groupValue")
                      val doclist = g.get("doclist").asInstanceOf[SolrDocumentList]
                      new Group(
                        groupValue = if (groupValue == null) "" else groupValue.toString,
                        numFound = doclist.getNumFound.toInt,
                        start = doclist.getStart.toInt,
                        documents = (doclist.asScala map {
                          case doc: SolrjSolrDocument => {
                            new SolrDocument(
                              writerType = writerType,
                              map = (doc.keySet.asScala.map {
                                case key => (key, new SolrDocumentValue(doc.get(key).toString))
                              }).toMap
                            )
                          }
                        }).toList
                      )
                    }
                  }).toList
                }
              }
            }
          }
        }
        new Groups(
          matches = matches,
          groups = groups
        )
      }
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }
}