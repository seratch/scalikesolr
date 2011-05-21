package com.github.seratch.scalikesolr

import org.specs.Specification
import java.net.URL
import request.query.facet._
import request.query.highlighting._
import request.query.morelikethis._
import request._
import org.slf4j.LoggerFactory
import query.{Sort, Query}

object SolrClientSpec extends Specification {

  val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.SolrClientSpec")
  val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient()

  "doQuery" should {

    "be available" in {

      val request = new QueryRequest(Query("author:Rick"))
      val response = client.doQuery(request)
      log.debug(response.toString)
      response.responseHeader mustNot beNull
      response.responseHeader.status must greaterThanOrEqualTo(0)
      response.responseHeader.qTime must greaterThanOrEqualTo(0)
      response.responseHeader.params mustNot beNull
      response.response.documents.size must beEqual(2)

      log.debug("-----------------------------")
      log.debug(response.response.documents.toString)
      response.response.documents foreach {
        case doc => {
          log.debug(doc.get("id").toString()) // "978-1423103349"
          log.debug(doc.get("cat").toListOrElse(Nil).toString) // List(book, hardcover)
          log.debug(doc.get("title").toString()) // "The Lightning Thief"
          log.debug(doc.get("pages_i").toIntOrElse(0).toString) // 384
          log.debug(doc.get("price").toDoubleOrElse(0.0).toString) // 12.5
        }
      }

    }

    "be available in JSON" in {

      val request = new QueryRequest(
        writerType = WriterType.JSON,
        query = Query("author:Rick"))
      val response = client.doQuery(request)
      log.debug(response.toString)
      response.responseHeader mustNot beNull
      response.responseHeader.status must greaterThanOrEqualTo(0)
      response.responseHeader.qTime must greaterThanOrEqualTo(0)
      response.response.documents.size must beEqual(2)

      log.debug("-----------------------------")
      log.debug(response.response.documents.toString)
      response.response.documents foreach {
        case doc => {
          log.debug(doc.get("id").toString()) // "978-1423103349"
          log.debug(doc.get("cat").toListOrElse(Nil).toString) // List(book, hardcover)
          log.debug(doc.get("title").toString()) // "The Lightning Thief"
          log.debug(doc.get("pages_i").toIntOrElse(0).toString) // 384
          log.debug(doc.get("price").toDoubleOrElse(0.0).toString) // 12.5
        }
      }

    }

    "be available with HighlightingParams" in {
      val request = new QueryRequest(
        query = Query("author:Rick"),
        highlighting = HighlightingParams(true))
      request.sort = Sort("page_i desc")
      val response = client.doQuery(request)
      log.debug(response.toString)
      response.responseHeader.status must greaterThanOrEqualTo(0)
      response.responseHeader.qTime must greaterThanOrEqualTo(0)
      response.response.documents.size must beEqual(2)
      response.response.documents foreach {
        doc => log.debug(response.highlightings.get(doc.get("id").toString).toString)
      }
      response.highlightings.size must beEqual(2)
      log.debug("-----------------------------")
      log.debug(response.highlightings.toString)
      response.highlightings.keys() foreach {
        case key => {
          log.debug(key + " -> " + response.highlightings.get(key).get("author").toString)
          // 978-0641723445 -> <em>Rick</em> Riordan
        }
      }
    }

    "be available with HighlightingParams in JSON" in {
      val request = new QueryRequest(
        writerType = WriterType.JSON,
        query = Query("author:Rick"),
        sort = Sort("page_i desc"),
        highlighting = HighlightingParams(true))
      val response = client.doQuery(request)
      log.debug(response.toString)
      response.responseHeader.status must greaterThanOrEqualTo(0)
      response.responseHeader.qTime must greaterThanOrEqualTo(0)
      response.response.documents.size must beEqual(2)
      response.response.documents foreach {
        doc => log.debug(response.highlightings.get(doc.get("id").toString).toString)
      }
      response.highlightings.size must beEqual(2)
      log.debug("-----------------------------")
      log.debug(response.highlightings.toString)
      response.highlightings.keys() foreach {
        case key => {
          log.debug(key + "->" + response.highlightings.get(key).get("author").toString)
        }
      }
    }

    "be available with MoreLikeThisParams" in {
      val request = new QueryRequest(
        query = Query("author:Rick"),
        moreLikeThis = MoreLikeThisParams(true, 3, FieldsToUseForSimilarity("body")))
      val response = client.doQuery(request)
      log.debug(response.toString)
      response.responseHeader.status must greaterThanOrEqualTo(0)
      response.responseHeader.qTime must greaterThanOrEqualTo(0)
      response.response.documents.size must beEqual(2)
      log.debug("-----------------------------")
      log.debug(response.moreLikeThis.toString)
      response.response.documents foreach {
        doc => {
          val id = doc.get("id").toString
          log.debug(id + "->" + response.moreLikeThis.get(id).toString)
        }
      }
    }

    "be available with MoreLikeThisParams in JSON" in {
      val request = new QueryRequest(
        writerType = WriterType.JSON,
        query = Query("author:Rick"),
        moreLikeThis = MoreLikeThisParams(true, 3, FieldsToUseForSimilarity("body")))
      val response = client.doQuery(request)
      log.debug(response.toString)
      response.responseHeader.status must greaterThanOrEqualTo(0)
      response.responseHeader.qTime must greaterThanOrEqualTo(0)
      response.response.documents.size must beEqual(2)
      log.debug("-----------------------------")
      log.debug(response.moreLikeThis.toString)
      response.response.documents foreach {
        doc => {
          val id = doc.get("id").toString
          log.debug(id + "->" + response.moreLikeThis.get(id).toString)
        }
      }
    }

    "be available with FacetParams" in {
      val request = new QueryRequest(
        query = Query("author:Rick"),
        facet = new FacetParams(enabled = true,
          params = List(new FacetParam(Param("facet.field"), Value("title")))
        )
      )
      val response = client.doQuery(request)
      log.debug(response.toString)
      response.responseHeader.status must greaterThanOrEqualTo(0)
      response.responseHeader.qTime must greaterThanOrEqualTo(0)
      response.response.documents.size must beEqual(2)
      log.debug("-----------------------------")
      log.debug("facetFields:" + response.facet.facetFields.toString)
      response.facet.facetFields.keys.size must beEqual(1)
      response.facet.facetFields.get("title").get.keys.size must beEqual(4)
      response.facet.facetFields.keys foreach {
        case key => {
          val facets = response.facet.facetFields.getOrElse(key, new SolrDocument())
          facets.keys foreach {
            case facetKey => {
              log.debug(facetKey + "->" + facets.get(facetKey).toIntOrElse(0))
            }
          }
        }
      }
    }

    "be available with FacetParams in JSON" in {
      val request = new QueryRequest(
        writerType = WriterType.JSON,
        query = Query("author:Rick"),
        facet = new FacetParams(enabled = true,
          params = List(new FacetParam(Param("facet.field"), Value("title")))
        )
      )
      val response = client.doQuery(request)
      log.debug(response.toString)
      response.responseHeader.status must greaterThanOrEqualTo(0)
      response.responseHeader.qTime must greaterThanOrEqualTo(0)
      response.response.documents.size must beEqual(2)
      log.debug("facetFields:" + response.facet.facetFields.toString)
      response.facet.facetFields.keys.size must beEqual(1)
      response.facet.facetFields.get("title").get.keys.size must beEqual(4)
    }

  }

  //    "doDIHCommand" should {
  //
  //      "be available" in {
  //        val request = new DIHCommandRequest(command = "delta-import")
  //        val response = client.doDIHCommand(request)
  //        println(response)
  //        println(response.initArgs)
  //        println(response.command)
  //        println(response.status)
  //        println(response.importResponse)
  //        println(response.statusMessages)
  //      }
  //
  //    }

  "doAddDocumentsAndCommit" should {

    "be available" in {
      val request = new AddRequest()
      val doc1 = SolrDocument(
        writerType = WriterType.JSON,
        rawBody = """
        {"id" : "978-0641723445",
         "cat" : ["book","hardcover"],
         "title" : "The Lightning Thief",
         "author" : "Rick Riordan",
         "series_t" : "Percy Jackson and the Olympians",
         "sequence_i" : 1,
         "genre_s" : "fantasy",
         "inStock" : true,
         "price" : 12.50,
         "pages_i" : 384
       }
       """
      )
      val doc2 = SolrDocument(
        writerType = WriterType.JSON,
        rawBody = """
       {
          "id" : "978-1423103349",
          "cat" : ["book","paperback"],
          "title" : "The Sea of Monsters",
          "author" : "Rick Riordan",
          "series_t" : "Percy Jackson and the Olympians",
          "sequence_i" : 2,
          "genre_s" : "fantasy",
          "inStock" : true,
          "price" : 6.49,
          "pages_i" : 304
        }
      """
      )
      request.documents = List(doc1, doc2)
      val response = client.doAddDocuments(request)
      client.doCommit(new UpdateRequest())
      log.debug(response.toString)
      response.responseHeader.status must greaterThanOrEqualTo(0)
      response.responseHeader.qTime must greaterThanOrEqualTo(0)
    }

  }

  "doCommit" should {

    "be available" in {
      val request = new UpdateRequest()
      val response = client.doCommit(request)
      log.debug(response.toString)
      response.responseHeader.status must greaterThanOrEqualTo(0)
      response.responseHeader.qTime must greaterThanOrEqualTo(0)
    }

  }

  "doRollback" should {

    "be available" in {
      val request = new UpdateRequest()
      val response = client.doRollback(request)
      log.debug(response.toString)
      response.responseHeader.status must greaterThanOrEqualTo(0)
      response.responseHeader.qTime must greaterThanOrEqualTo(0)
    }

  }

  "doPing" should {

    "be available" in {
      val request = new PingRequest()
      val response = client.doPing(request)
      log.debug(response.toString)
      response.responseHeader.status must greaterThanOrEqualTo(0)
      response.responseHeader.qTime must greaterThanOrEqualTo(0)
      response.status must beEqual("OK")
    }

  }

  "doPing in JSON" should {

    "be available" in {
      val request = new PingRequest(writerType = WriterType.JSON)
      val response = client.doPing(request)
      log.debug(response.toString)
      response.responseHeader.status must greaterThanOrEqualTo(0)
      response.responseHeader.qTime must greaterThanOrEqualTo(0)
      response.status must beEqual("OK")
    }

  }

}