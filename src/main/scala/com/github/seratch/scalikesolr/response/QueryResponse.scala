package com.github.seratch.scalikesolr.response

import parser.ResponseParser
import reflect.BeanProperty

import xml._

import util.parsing.json.JSON
import com.github.seratch.scalikesolr.util.JSONUtil

import com.github.seratch.scalikesolr.request.common._
import com.github.seratch.scalikesolr.response.common._
import com.github.seratch.scalikesolr.response.query._
import com.github.seratch.scalikesolr.{SolrDocumentValue, SolrDocument}

case class QueryResponse(@BeanProperty val writerType: WriterType = WriterType.Standard,
                         @BeanProperty val rawBody: String = "") {

  @BeanProperty lazy val responseHeader: ResponseHeader = ResponseParser.getResponseHeader(writerType, rawBody)

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
        val jsonMap: Map[String, Option[Any]] = JSON.parseFull(rawBody).getOrElse(Map()).asInstanceOf[Map[String, Option[Any]]]
        val response: Map[String, Option[Any]] = jsonMap.get("response").get.asInstanceOf[Map[String, Option[Any]]]
        val numFound = JSONUtil.normalizeNum(response.get("numFound").getOrElse("0").toString).toInt
        val start = JSONUtil.normalizeNum(response.get("start").getOrElse("0").toString).toInt
        val docs: List[Map[String, Option[Any]]] = response.get("docs").getOrElse(Nil).asInstanceOf[List[Map[String, Option[Any]]]]
        val documents: Seq[SolrDocument] = docs map {
          case doc: Map[String, Option[Any]] => {
            val docMap = new collection.mutable.HashMap[String, SolrDocumentValue]
            doc.keysIterator.foreach {
              case key => docMap.update(key, new SolrDocumentValue(doc.getOrElse(key, "").toString))
            }
            new SolrDocument(writerType = writerType, map = docMap.toMap)
          }
        }
        new Response(numFound, start, documents.toList)
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
        val jsonMap: Map[String, Option[Any]] = JSON.parseFull(rawBody).getOrElse(Map()).asInstanceOf[Map[String, Option[Any]]]
        val highlighting: Map[String, Option[Any]] = jsonMap.get("highlighting").getOrElse(Map()).asInstanceOf[Map[String, Option[Any]]]
        val hashMap = new collection.mutable.HashMap[String, SolrDocument]
        highlighting.keysIterator.foreach {
          case key => {
            val doc: Map[String, Option[Any]] = highlighting.get(key).getOrElse(Map()).asInstanceOf[Map[String, Option[Any]]]
            val docHashMap = new collection.mutable.HashMap[String, SolrDocumentValue]
            doc.keysIterator.foreach {
              case docKey => {
                docHashMap.update(docKey, new SolrDocumentValue(doc.getOrElse(docKey, "").toString))
              }
            }
            hashMap.update(key, new SolrDocument(
              writerType = WriterType.JSON,
              map = docHashMap.toMap
            ))
          }
        }
        new Highlightings(
          highlightings = hashMap.toMap
        )
      }
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

  @BeanProperty lazy val moreLikeThis: MoreLikeThis = {
    writerType match {
      case WriterType.Standard => {
        val xml = XML.loadString(rawBody)
        val hashMap = new collection.mutable.HashMap[String, SolrDocument]
        val mltCandidates = (xml \ "lst").filter(lst => (lst \ "@name").text == "moreLikeThis")
        if (mltCandidates.size > 0) {
          val mlt = mltCandidates(0)
          (mlt \ "result") foreach {
            case result: Node => {
              val element = new collection.mutable.HashMap[String, SolrDocumentValue]
              (result \ "doc") foreach {
                doc => element.update((doc \ "@name").text, new SolrDocumentValue(doc.text))
              }
              element.update("numFound", SolrDocumentValue((result \ "@numFound").text))
              element.update("start", SolrDocumentValue((result \ "@start").text))
              hashMap.update((result \ "@name").text, SolrDocument(map = element.toMap))
            }
          }
        }
        new MoreLikeThis(
          moreLikeThis = hashMap.toMap
        )
      }
      case WriterType.JSON => {
        val jsonMap: Map[String, Option[Any]] = JSON.parseFull(rawBody).getOrElse(Map()).asInstanceOf[Map[String, Option[Any]]]
        val moreLikeThis: Map[String, Option[Any]] = jsonMap.get("moreLikeThis").getOrElse(Map()).asInstanceOf[Map[String, Option[Any]]]
        val hashMap = new collection.mutable.HashMap[String, SolrDocument]
        moreLikeThis.keysIterator.foreach {
          case key => {
            val doc: Map[String, Option[Any]] = moreLikeThis.get(key).getOrElse(Map()).asInstanceOf[Map[String, Option[Any]]]
            val docHashMap = new collection.mutable.HashMap[String, SolrDocumentValue]
            doc.keysIterator.foreach {
              case docKey => {
                docHashMap.update(docKey, new SolrDocumentValue(doc.getOrElse(docKey, "").toString))
              }
            }
            hashMap.update(key, new SolrDocument(
              writerType = WriterType.JSON,
              map = docHashMap.toMap
            ))
          }
        }
        new MoreLikeThis(
          moreLikeThis = hashMap.toMap
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
            case facetQueries: Node if (facetQueries \ "@name").text == "facet_queries" => {
              (facetQueries \ "lst") foreach {
                query => {
                  val element = new collection.mutable.HashMap[String, SolrDocumentValue]
                  query.child foreach (value => element.update((value \ "@name").text, SolrDocumentValue(value.text)))
                  facetQueriesMap.update((query \ "@name").text, SolrDocument(map = element.toMap))
                }
              }
            }
            case facetFields: Node if (facetFields \ "@name").text == "facet_fields" => {
              facetFields.child foreach {
                field => {
                  val element = new collection.mutable.HashMap[String, SolrDocumentValue]
                  field.child foreach (value => element.update((value \ "@name").text, SolrDocumentValue(value.text)))
                  facetFieldsMap.update((field \ "@name").text, SolrDocument(map = element.toMap))
                }
              }
            }
            case facetDates: Node if (facetDates \ "@name").text == "facet_dates" => {
              (facetDates \ "lst") foreach {
                date => {
                  val element = new collection.mutable.HashMap[String, SolrDocumentValue]
                  date.child foreach (value => element.update((value \ "@name").text, SolrDocumentValue(value.text)))
                  facetDatesMap.update((date \ "@name").text, SolrDocument(map = element.toMap))
                }
              }
            }
            case facetRanges: Node if (facetRanges \ "@name").text == "facet_ranges" => {
              (facetRanges \ "lst") foreach {
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
        new Facet(
          facetQueries = facetQueriesMap.toMap,
          facetFields = facetFieldsMap.toMap,
          facetDates = facetDatesMap.toMap,
          facetRanges = facetRangesMap.toMap
        )
      }
      case WriterType.JSON => {

        def parseFacetsToMap(facets: List[Any], docHashMap: collection.mutable.HashMap[String, SolrDocumentValue]): Unit = {
          facets.size match {
            case 0 =>
            case _ => {
              val kv = facets.take(2)
              docHashMap.update(kv(0).toString, new SolrDocumentValue(kv(1).toString))
              parseFacetsToMap(facets.drop(2), docHashMap)
            }
          }
        }

        val facetQueriesMap = new collection.mutable.HashMap[String, SolrDocument]
        val facetFieldsMap = new collection.mutable.HashMap[String, SolrDocument]
        val facetDatesMap = new collection.mutable.HashMap[String, SolrDocument]
        val facetRangesMap = new collection.mutable.HashMap[String, SolrDocument]

        val jsonMap: Map[String, Option[Any]] = JSON.parseFull(rawBody).getOrElse(Map()).asInstanceOf[Map[String, Option[Any]]]
        val facetCounts: Map[String, Option[Any]] = jsonMap.get("facet_counts").getOrElse(Map()).asInstanceOf[Map[String, Option[Any]]]
        val hashMap = new collection.mutable.HashMap[String, SolrDocument]
        facetCounts.keysIterator.foreach {
          case key if key == "facet_queries" => {
            val doc: Map[String, Option[Any]] = facetCounts.get(key).getOrElse(Map()).asInstanceOf[Map[String, Option[Any]]]
            doc.keysIterator.foreach {
              case docKey => {
                val docHashMap = new collection.mutable.HashMap[String, SolrDocumentValue]
                val facets: List[Any] = doc.getOrElse(docKey, Nil).asInstanceOf[List[Any]]
                parseFacetsToMap(facets, docHashMap)
                facetQueriesMap.update(docKey, new SolrDocument(
                  writerType = WriterType.JSON,
                  map = docHashMap.toMap
                ))
              }
            }
          }
          case key if key == "facet_fields" => {
            val doc: Map[String, Option[Any]] = facetCounts.get(key).getOrElse(Map()).asInstanceOf[Map[String, Option[Any]]]
            doc.keysIterator.foreach {
              case docKey => {
                val docHashMap = new collection.mutable.HashMap[String, SolrDocumentValue]
                val facets: List[Any] = doc.getOrElse(docKey, Nil).asInstanceOf[List[Any]]
                parseFacetsToMap(facets, docHashMap)
                facetFieldsMap.update(docKey, new SolrDocument(
                  writerType = WriterType.JSON,
                  map = docHashMap.toMap
                ))
              }
            }
          }
          case key if key == "facet_dates" => {
            val doc: Map[String, Option[Any]] = facetCounts.get(key).getOrElse(Map()).asInstanceOf[Map[String, Option[Any]]]
            doc.keysIterator.foreach {
              case docKey => {
                val docHashMap = new collection.mutable.HashMap[String, SolrDocumentValue]
                val facets: List[Any] = doc.getOrElse(docKey, Nil).asInstanceOf[List[Any]]
                parseFacetsToMap(facets, docHashMap)
                facetDatesMap.update(docKey, new SolrDocument(
                  writerType = WriterType.JSON,
                  map = docHashMap.toMap
                ))
              }
            }
          }
          case key if key == "facet_ranges" => {
            val doc: Map[String, Option[Any]] = facetCounts.get(key).getOrElse(Map()).asInstanceOf[Map[String, Option[Any]]]
            doc.keysIterator.foreach {
              case docKey => {
                val docHashMap = new collection.mutable.HashMap[String, SolrDocumentValue]
                val facets: List[Any] = doc.getOrElse(docKey, Nil).asInstanceOf[List[Any]]
                parseFacetsToMap(facets, docHashMap)
                facetRangesMap.update(docKey, new SolrDocument(
                  writerType = WriterType.JSON,
                  map = docHashMap.toMap
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
