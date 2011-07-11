package com.github.seratch.scalikesolr.client

import java.net.URL
import org.slf4j.LoggerFactory
import org.junit._
import org.scalatest.Assertions
import com.github.seratch.scalikesolr.request.common.WriterType
import com.github.seratch.scalikesolr.request.query.highlighting.HighlightingParams
import com.github.seratch.scalikesolr.request.query.{Sort, Query}
import com.github.seratch.scalikesolr.request.query.morelikethis.{FieldsToUseForSimilarity, MoreLikeThisParams}
import com.github.seratch.scalikesolr.{SolrDocument, Solr}
import com.github.seratch.scalikesolr.request.query.facet.{Param, Value, FacetParam, FacetParams}
import com.github.seratch.scalikesolr.request.{UpdateRequest, AddRequest, QueryRequest}

class SolrClient_doQueryTest extends Assertions {

  val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.SolrClientSpec")
  val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient()

  @Before
  def parepare() {
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
  }

  @Test
  def available() {
    val request = new QueryRequest(Query("author:Rick"))
    val response = client.doQuery(request)
    log.debug(response.toString)
    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.responseHeader.params != null)
    assert(response.response.documents.size == 2)

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

  @Test
  def availableWithMultibyteQuery() {
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

  @Test
  def availableInJSON() {
    val request = new QueryRequest(
      writerType = WriterType.JSON,
      query = Query("author:Rick"))
    val response = client.doQuery(request)
    log.debug(response.toString)


    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.response.documents.size == 2)

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

  @Test
  def availableWithHighlightingParams() {
    val request = new QueryRequest(
      query = Query("author:Rick"),
      highlighting = HighlightingParams(true))
    request.sort = Sort("page_i desc")
    val response = client.doQuery(request)
    log.debug(response.toString)
    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.response.documents.size == 2)
    response.response.documents foreach {
      doc => log.debug(response.highlightings.get(doc.get("id").toString).toString)
    }
    assert(response.highlightings.size == 2)
    log.debug("-----------------------------")
    log.debug(response.highlightings.toString)
    response.highlightings.keys() foreach {
      case key => {
        log.debug(key + " -> " + response.highlightings.get(key).get("author").toString)
        // 978-0641723445 -> <em>Rick</em> Riordan
      }
    }
  }

  @Test
  def availableInJSONWithHighlightingParams() {
    val request = new QueryRequest(
      writerType = WriterType.JSON,
      query = Query("author:Rick"),
      sort = Sort("page_i desc"),
      highlighting = HighlightingParams(true))
    val response = client.doQuery(request)
    log.debug(response.toString)
    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.response.documents.size == 2)
    response.response.documents foreach {
      doc => log.debug(response.highlightings.get(doc.get("id").toString).toString)
    }
    assert(response.highlightings.size == 2)
    log.debug("-----------------------------")
    log.debug(response.highlightings.toString)
    response.highlightings.keys() foreach {
      case key => {
        log.debug(key + "->" + response.highlightings.get(key).get("author").toString)
      }
    }
  }

  @Test
  def availableWithMoreLikeThisParams() {
    val request = new QueryRequest(
      query = Query("author:Rick"),
      moreLikeThis = MoreLikeThisParams(true, 3, FieldsToUseForSimilarity("body")))
    val response = client.doQuery(request)
    log.debug(response.toString)
    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.response.documents.size == 2)
    log.debug("-----------------------------")
    log.debug(response.moreLikeThis.toString)
    response.response.documents foreach {
      doc => {
        val id = doc.get("id").toString
        log.debug(id + "->" + response.moreLikeThis.getList(id).toString)
      }
    }
  }

  @Test
  def availableInJSONWithMoreLikeThisParams() {
    val request = new QueryRequest(
      writerType = WriterType.JSON,
      query = Query("author:Rick"),
      moreLikeThis = MoreLikeThisParams(true, 3, FieldsToUseForSimilarity("body")))
    val response = client.doQuery(request)
    log.debug(response.toString)
    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.response.documents.size == 2)
    log.debug("-----------------------------")
    log.debug(response.moreLikeThis.toString)
    response.response.documents foreach {
      doc => {
        val id = doc.get("id").toString
        log.debug(id + "->" + response.moreLikeThis.getList(id).toString)
      }
    }
  }

  @Test
  def availableWithFacetParams() {
    val request = new QueryRequest(
      query = Query("author:Rick"),
      facet = new FacetParams(enabled = true,
        params = List(new FacetParam(Param("facet.field"), Value("title")))
      )
    )
    val response = client.doQuery(request)
    log.debug(response.toString)
    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.response.documents.size == 2)
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

  @Test
  def availableInJSONWithFacetParams() {
    val request = new QueryRequest(
      writerType = WriterType.JSON,
      query = Query("author:Rick"),
      facet = new FacetParams(enabled = true,
        params = List(new FacetParam(Param("facet.field"), Value("title")))
      )
    )
    val response = client.doQuery(request)
    log.debug(response.toString)
    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.response.documents.size == 2)
    log.debug("facetFields:" + response.facet.facetFields.toString)
    // Solr 3.2: Map(title -> SolrDocument(WriterType(standard),,Map(thief -> 1, sea -> 1, monster -> 1, lightn -> 1)))
    // Solr 3.3: Map(title -> SolrDocument(WriterType(standard),,Map(sea -> 1, thief -> 1, monsters -> 1, lightning -> 1, of -> 1, the -> 2)))
    assert(response.facet.facetFields.keys.size == 1)
    // Solr 3.2: response.facet.facetFields.get("title").get.keys.size must beEqual(4)
    // Solr 3.3: response.facet.facetFields.get("title").get.keys.size must beEqual(6)
    assert(response.facet.facetFields.get("title").get.keys.size >= 4)
  }

}