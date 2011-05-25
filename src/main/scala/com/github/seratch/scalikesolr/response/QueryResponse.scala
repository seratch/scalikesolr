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
            doc.keysIterator.foreach {
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
                doc.keysIterator.foreach {
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

        val facetCounts = JSONUtil.toMap(jsonMapFromRawBody.get("facet_counts"))
        facetCounts.keysIterator.foreach {
          case key if key == "facet_queries" => {
            val doc = JSONUtil.toMap(facetCounts.get(key))
            doc.keysIterator.foreach {
              case docKey => {
                val docHashMap = new collection.mutable.HashMap[String, SolrDocumentValue]
                val facets = doc.getOrElse(docKey, Nil).asInstanceOf[List[Any]]
                parseFacetsToMap(facets, docHashMap)
                facetQueriesMap.update(docKey, new SolrDocument(
                  writerType = WriterType.JSON,
                  map = docHashMap.toMap
                ))
              }
            }
          }
          case key if key == "facet_fields" => {
            val doc = JSONUtil.toMap(facetCounts.get(key))
            doc.keysIterator.foreach {
              case docKey => {
                val docHashMap = new collection.mutable.HashMap[String, SolrDocumentValue]
                val facets = doc.getOrElse(docKey, List()).asInstanceOf[List[Any]]
                parseFacetsToMap(facets, docHashMap)
                facetFieldsMap.update(docKey, new SolrDocument(
                  writerType = WriterType.JSON,
                  map = docHashMap.toMap
                ))
              }
            }
          }
          case key if key == "facet_dates" => {
            val doc = JSONUtil.toMap(facetCounts.get(key))
            doc.keysIterator.foreach {
              case docKey => {
                val docHashMap = new collection.mutable.HashMap[String, SolrDocumentValue]
                val facets = doc.getOrElse(docKey, List()).asInstanceOf[List[Any]]
                parseFacetsToMap(facets, docHashMap)
                facetDatesMap.update(docKey, new SolrDocument(
                  writerType = WriterType.JSON,
                  map = docHashMap.toMap
                ))
              }
            }
          }
          case key if key == "facet_ranges" => {
            val doc = JSONUtil.toMap(facetCounts.get(key))
            doc.keysIterator.foreach {
              case docKey => {
                val docHashMap = new collection.mutable.HashMap[String, SolrDocumentValue]
                val facets = doc.getOrElse(docKey, List()).asInstanceOf[List[Any]]
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
