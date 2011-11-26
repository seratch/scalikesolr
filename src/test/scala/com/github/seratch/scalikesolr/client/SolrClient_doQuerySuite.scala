package com.github.seratch.scalikesolr.client

import java.net.URL
import org.slf4j.LoggerFactory
import com.github.seratch.scalikesolr.request.common.WriterType
import com.github.seratch.scalikesolr.request.query.highlighting.HighlightingParams
import com.github.seratch.scalikesolr.request.query.morelikethis.{FieldsToUseForSimilarity, MoreLikeThisParams}
import com.github.seratch.scalikesolr.{SolrDocument, Solr}
import com.github.seratch.scalikesolr.request.query.facet.{Param, Value, FacetParam, FacetParams}
import com.github.seratch.scalikesolr.request.query.group.{AsMainResultWhenUsingSimpleFormat, GroupFormat, GroupField, GroupParams}
import com.github.seratch.scalikesolr.request.{UpdateRequest, AddRequest, QueryRequest}
import org.joda.time.DateTime
import com.github.seratch.scalikesolr.request.query.{MaximumRowsReturned, Sort, Query}
import com.github.seratch.scalikesolr.util.Log
import com.github.seratch.scalikesolr.request.query.distributedsearch.DistributedSearchParams
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.impl.{BinaryResponseParser, CommonsHttpSolrServer}

@RunWith(classOf[JUnitRunner])
class SolrClient_doQuerySuite extends FunSuite {

  type ? = this.type

  val log = new Log(LoggerFactory.getLogger(classOf[SolrClient_doQuerySuite]))
  val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient()

  test("parepare") {
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
    client.doAddDocuments(request)
    client.doCommit(new UpdateRequest())
  }

  test("available") {
    val request = new QueryRequest(Query("author:Rick"))
    val response = client.doQuery(request)
    log.debug(response.toString)

    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.responseHeader.params != null)

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
    assert(response.response.documents.size == 10)
  }

  test("availableWithDistributedSearch") {
    val client = Solr.httpServer(new URL("http://localhost:8984/solr")).newClient()
    val request = new QueryRequest(Query("author:Rick"))
    request.shards = DistributedSearchParams(shards = List("localhost:8983/solr"))
    val response = client.doQuery(request)
    log.debug(response.toString)

    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.responseHeader.params != null)

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
    assert(response.response.documents.size == 10)
  }

  test("availableWithMultibyteQuery") {
    val request = new QueryRequest(Query("author:日本人"))
    val response = client.doQuery(request)
    log.debug(response.toString)
    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.responseHeader.params != null)

    log.debug("-----------------------------")
    log.debug(response.toString)
  }

  test("availableWithGroupParams") {
    val request = new QueryRequest(
      query = Query("genre_s:fantasy")
    )
    request.group = GroupParams(enabled = true, field = GroupField("author_t"))
    request.sort = Sort("page_i desc")
    val response = client.doQuery(request)
    log.debug(response.toString)
    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    log.debug("-----------------------------")
    response.groups.groups foreach {
      case group => {
        log.debug(group.groupValue + " -> " + group.documents.toString)
      }
    }
    log.debug(response.groups.toString)
    assert(response.groups.groups.size == 3)
    assert(response.groups.groups.apply(0).numFound > 0)
    assert(response.groups.groups.apply(0).start == 0)
    assert(response.groups.groups.apply(1).numFound > 0)
    assert(response.groups.groups.apply(1).start == 0)
    assert(response.groups.groups.apply(2).numFound > 0)
    assert(response.groups.groups.apply(2).start == 0)
  }

  test("availableWithGroupParamsWithSimpleFormat") {
    val request = new QueryRequest(
      query = Query("genre_s:fantasy")
    )
    request.group = GroupParams(enabled = true, field = GroupField("author_t"), format = GroupFormat("simple"))
    request.sort = Sort("page_i desc")
    val response = client.doQuery(request)
    log.debug(response.toString)
    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    log.debug("-----------------------------")
    log.debug(response.groups.toString)
    response.groups.groups foreach {
      case group => {
        log.debug(group.groupValue + " -> " + group.documents.toString)
      }
    }
    assert(response.groups.groups.size == 1)
    assert(response.groups.groups.apply(0).numFound > 10)
    assert(response.groups.groups.apply(0).start == 0)
  }

  test("availableWithGroupParamsWithSimpleFormatAndMain") {
    val request = new QueryRequest(
      query = Query("genre_s:fantasy")
    )
    request.group = GroupParams(
      enabled = true,
      field = GroupField("author_t"),
      format = GroupFormat("simple"),
      main = AsMainResultWhenUsingSimpleFormat(true)
    )
    request.sort = Sort("page_i desc")
    val response = client.doQuery(request)
    log.debug(response.toString)
    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.response.documents.size == 3)
    log.debug("-----------------------------")
    log.debug(response.groups.toString)
    response.response.documents foreach {
      case doc => log.debug(doc.toString)
    }
    assert(response.groups.groups.size == 0)
  }

  test("availableWithHighlightingParams") {
    val request = new QueryRequest(
      query = Query("author:Rick")
    )
    request.highlighting = HighlightingParams(true)
    request.sort = Sort("page_i desc")
    val response = client.doQuery(request)
    log.debug(response.toString)
    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.response.documents.size == 10)
    response.response.documents foreach {
      doc => log.debug(response.highlightings.get(doc.get("id").toString).toString)
    }
    assert(response.highlightings.size == 10)
    log.debug("-----------------------------")
    log.debug(response.highlightings.toString)
    response.highlightings.keys() foreach {
      case key => {
        log.debug(key + " -> " + response.highlightings.get(key).get("author").toString)
        // 978-0641723445 -> <em>Rick</em> Riordan
      }
    }
  }

  test("availableWithMoreLikeThisParams") {
    val request = new QueryRequest(
      query = Query("author:Rick")
    )
    request.moreLikeThis = MoreLikeThisParams(true, 3, FieldsToUseForSimilarity("body"))
    val response = client.doQuery(request)
    log.debug(response.toString)
    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.response.documents.size == 10)
    log.debug("-----------------------------")
    log.debug(response.moreLikeThis.toString)
    response.response.documents foreach {
      doc => {
        val id = doc.get("id").toString
        log.debug(id + "->" + response.moreLikeThis.getList(id).toString)
      }
    }
  }

  test("availableWithFacetParams") {
    val request = new QueryRequest(
      query = Query("author:Rick")
    )
    request.facet = new FacetParams(enabled = true,
      params = List(new FacetParam(Param("facet.field"), Value("title")))
    )
    val response = client.doQuery(request)
    log.debug(response.toString)
    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.response.documents.size == 10)
    log.debug("-----------------------------")
    log.debug("facetFields:" + response.facet.facetFields.toString)
    // Solr 3.2: Map(title -> SolrDocument(WriterType(standard),,Map(thief -> 1, sea -> 1, monster -> 1, lightn -> 1)))
    // Solr 3.3: Map(title -> SolrDocument(WriterType(standard),,Map(sea -> 1, thief -> 1, monsters -> 1, lightning -> 1, of -> 1, the -> 2)))
    assert(response.facet.facetFields.keys.size == 1)
    // Solr 3.2: response.facet.facetFields.get("title").get.keys.size must beEqual(4)
    // Solr 3.3: response.facet.facetFields.get("title").get.keys.size must beEqual(6)
    assert(response.facet.facetFields.get("title").get.keys.size >= 4)
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

  test("checkPerformanceOfFetching") {

    val log = new Log(LoggerFactory.getLogger(classOf[SolrClient_doQuerySuite]))
    log.isDebugEnabled = false
    val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient(log)
    val currentCount = client.doQuery(new QueryRequest(
      query = Query("author:Rick"),
      maximumRowsReturned = MaximumRowsReturned(1))
    ).response.numFound

    if (currentCount < 100) {
      val addReq = new AddRequest()
      val ids = (Range(0, 10000) map {
        i => "unittest" + i.toString
      }).toList
      addReq.documents = ids map {
        id => SolrDocument(
          writerType = WriterType.JSON,
          rawBody = """{"id" : """" + id + """",
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
      }
      client.doAddDocuments(addReq)
      client.doCommit(new UpdateRequest)
    }

    val request = new QueryRequest(
      query = Query("author:Rick"),
      maximumRowsReturned = MaximumRowsReturned(1000)
    )
    val xmlStart = new DateTime()
    log.info("xml start  :" + xmlStart)
    client.doQuery(request)
    val xmlEnd = new DateTime()
    log.info("xml end    :" + xmlEnd)

    request.writerType = WriterType.JSON
    val jsonStart = new DateTime()
    log.info("json start :" + jsonStart)
    client.doQuery(request)
    val jsonEnd = new DateTime()
    log.info("json end   :" + jsonEnd)

    request.writerType = WriterType.JavaBinary
    val javabinStart = new DateTime()
    log.info("javabin start :" + javabinStart)
    client.doQuery(request)
    val javabinEnd = new DateTime()
    log.info("javabin end   :" + javabinEnd)

    log.info("xml result  :" + (xmlEnd.getMillis - xmlStart.getMillis))
    log.info("json result :" + (jsonEnd.getMillis - jsonStart.getMillis))
    log.info("javabin result :" + (javabinEnd.getMillis - javabinStart.getMillis))

  }

  test("checkPerformanceOfLoadingDocuments") {

    val log = new Log(LoggerFactory.getLogger(classOf[SolrClient_doQuerySuite]))
    log.isDebugEnabled = false
    val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient(log)
    val currentCount = client.doQuery(new QueryRequest(
      query = Query("author:Rick"),
      maximumRowsReturned = MaximumRowsReturned(1))
    ).response.numFound

    if (currentCount < 100) {
      val addReq = new AddRequest()
      val ids = (Range(0, 10000) map {
        i => "unittest" + i.toString
      }).toList
      addReq.documents = ids map {
        id => SolrDocument(
          writerType = WriterType.JSON,
          rawBody = """{"id" : """" + id + """",
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
      }
      client.doAddDocuments(addReq)
      client.doCommit(new UpdateRequest)
    }

    val request = new QueryRequest(
      query = Query("author:Rick"),
      maximumRowsReturned = MaximumRowsReturned(1000)
    )

    val xmlResponse = client.doQuery(request)

    val xmlStart = new DateTime()
    log.info("xml start  :" + xmlStart)
    val docsFromXML = xmlResponse.response.documents
    val xmlEnd = new DateTime()
    log.info("xml end    :" + xmlEnd)

    request.writerType = WriterType.JSON
    val jsonResponse = client.doQuery(request)

    val jsonStart = new DateTime()
    log.info("json start :" + jsonStart)
    val docsFromJSON = jsonResponse.response.documents
    val jsonEnd = new DateTime()
    log.info("json end   :" + jsonEnd)

    request.writerType = WriterType.JavaBinary
    val javabinResponse = client.doQuery(request)

    val javabinStart = new DateTime()
    log.info("javabin start :" + javabinStart)
    val docsFromJavaBin = javabinResponse.response.documents
    val javabinEnd = new DateTime()
    log.info("javabin end   :" + javabinEnd)

    assert(docsFromXML.size > 0)
    assert(docsFromJSON.size > 0)
    assert(docsFromJavaBin.size > 0)

    log.info("xml result  :" + (xmlEnd.getMillis - xmlStart.getMillis))
    log.info("json result :" + (jsonEnd.getMillis - jsonStart.getMillis))
    log.info("javabin result :" + (javabinEnd.getMillis - javabinStart.getMillis))

  }

  test("checkPerformanceOfSimpleQuery") {

    val log = new Log(LoggerFactory.getLogger(classOf[SolrClient_doQuerySuite]))
    log.isDebugEnabled = false
    val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient(log)
    val currentCount = client.doQuery(new QueryRequest(
      query = Query("author:Rick"),
      maximumRowsReturned = MaximumRowsReturned(1))
    ).response.numFound

    if (currentCount < 100) {
      val addReq = new AddRequest()
      val ids = (Range(0, 10000) map {
        i => "unittest" + i.toString
      }).toList
      addReq.documents = ids map {
        id => SolrDocument(
          writerType = WriterType.JSON,
          rawBody = """{"id" : """" + id + """",
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
      }
      client.doAddDocuments(addReq)
      client.doCommit(new UpdateRequest)
    }

    val request = new QueryRequest(
      query = Query("author:Rick"),
      maximumRowsReturned = MaximumRowsReturned(1000)
    )

    val xmlStart = new DateTime()
    log.info("xml start  :" + xmlStart)
    val xmlResponse = client.doQuery(request)
    xmlResponse.responseHeader
    val docsFromXML = xmlResponse.response.documents
    val xmlEnd = new DateTime()
    log.info("xml end    :" + xmlEnd)

    request.writerType = WriterType.JSON

    val jsonStart = new DateTime()
    log.info("json start :" + jsonStart)
    val jsonResponse = client.doQuery(request)
    jsonResponse.responseHeader
    val docsFromJSON = jsonResponse.response.documents
    val jsonEnd = new DateTime()
    log.info("json end   :" + jsonEnd)

    request.writerType = WriterType.JavaBinary

    val javabinStart = new DateTime()
    log.info("javabin start :" + javabinStart)
    val javabinResponse = client.doQuery(request)
    javabinResponse.responseHeader
    val docsFromJavaBin = javabinResponse.response.documents
    val javabinEnd = new DateTime()
    log.info("javabin end   :" + javabinEnd)

    assert(docsFromXML.size > 0)
    assert(docsFromJSON.size > 0)
    assert(docsFromJavaBin.size > 0)

    log.info("xml result  :" + (xmlEnd.getMillis - xmlStart.getMillis))
    log.info("json result :" + (jsonEnd.getMillis - jsonStart.getMillis))
    log.info("javabin result :" + (javabinEnd.getMillis - javabinStart.getMillis))

  }

  test("checkPerformanceOfSimpleQuery vs Solrj") {

    val log = new Log(LoggerFactory.getLogger(classOf[SolrClient_doQuerySuite]))
    log.isDebugEnabled = false
    val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient(log)
    val currentCount = client.doQuery(new QueryRequest(
      query = Query("author:Rick"),
      maximumRowsReturned = MaximumRowsReturned(1))
    ).response.numFound

    if (currentCount < 100) {
      val addReq = new AddRequest()
      val ids = (Range(0, 10000) map {
        i => "unittest" + i.toString
      }).toList
      addReq.documents = ids map {
        id => SolrDocument(
          writerType = WriterType.JSON,
          rawBody = """{"id" : """" + id + """",
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
      }
      client.doAddDocuments(addReq)
      client.doCommit(new UpdateRequest)
    }

    // solrj
    {

      val start = new DateTime()
      log.info("solrj start :" + start)

      val server = new CommonsHttpSolrServer("http://localhost:8983/solr")
      server.setParser(new BinaryResponseParser())
      val query = new SolrQuery();
      query.setQuery("author:Rick");
      query.setRows(1000)

      val response = server.query(query)
      response.getResults

      val end = new DateTime()
      log.info("solrj end   :" + end)
      log.info("solrj result :" + (end.getMillis - start.getMillis))
    }

    // scalikesolr
    {

      val start = new DateTime()
      log.info("scalikesolr start :" + start)

      val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient(log)
      val request = new QueryRequest(
        query = Query("author:Rick"),
        maximumRowsReturned = MaximumRowsReturned(1000)
      )
      request.writerType = WriterType.JavaBinary

      val response = client.doQuery(request)
      response.responseHeader
      response.response.documents

      val end = new DateTime()
      log.info("scalikesolr end   :" + end)
      log.info("scalikesolr result :" + (end.getMillis - start.getMillis))
    }

    /**
     * solrj result :232
     * scalikesolr result :30
     *
     * solrj result :338
     * scalikesolr result :34
     *
     * solrj result :348
     * scalikesolr result :25
     */


  }

  test("checkPerformanceOfSimpleQuery(query only) vs Solrj") {

    val log = new Log(LoggerFactory.getLogger(classOf[SolrClient_doQuerySuite]))
    log.isDebugEnabled = false
    val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient(log)
    val currentCount = client.doQuery(new QueryRequest(
      query = Query("author:Rick"),
      maximumRowsReturned = MaximumRowsReturned(1))
    ).response.numFound

    if (currentCount < 100) {
      val addReq = new AddRequest()
      val ids = (Range(0, 10000) map {
        i => "unittest" + i.toString
      }).toList
      addReq.documents = ids map {
        id => SolrDocument(
          writerType = WriterType.JSON,
          rawBody = """{"id" : """" + id + """",
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
      }
      client.doAddDocuments(addReq)
      client.doCommit(new UpdateRequest)
    }

    // solrj
    {

      val server = new CommonsHttpSolrServer("http://localhost:8983/solr")
      server.setParser(new BinaryResponseParser())
      val query = new SolrQuery();
      query.setQuery("author:Rick");
      query.setRows(1000)

      val start = new DateTime()
      log.info("[query only] solrj start :" + start)

      val response = server.query(query)
      response.getResults

      val end = new DateTime()
      log.info("[query only] solrj end   :" + end)
      log.info("[query only] solrj result :" + (end.getMillis - start.getMillis))
    }

    // scalikesolr
    {

      val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient(log)
      val request = new QueryRequest(
        query = Query("author:Rick"),
        maximumRowsReturned = MaximumRowsReturned(1000)
      )
      request.writerType = WriterType.JavaBinary

      val start = new DateTime()
      log.info("[query only] scalikesolr start :" + start)

      val response = client.doQuery(request)
      response.responseHeader
      response.response.documents

      val end = new DateTime()
      log.info("[query only] scalikesolr end   :" + end)
      log.info("[query only] scalikesolr result :" + (end.getMillis - start.getMillis))
    }

    /**
     * [query only] solrj result :31
     * [query only] scalikesolr result :34
     *
     * [query only] solrj result :37
     * [query only] scalikesolr result :23
     *
     * [query only] solrj result :52
     * [query only] scalikesolr result :35
     */


  }

}