package com.github.seratch.scalikesolr.client

import java.net.URL
import org.slf4j.LoggerFactory
import org.junit._
import com.github.seratch.scalikesolr.request.common.WriterType
import com.github.seratch.scalikesolr.request.query.highlighting.HighlightingParams
import com.github.seratch.scalikesolr.request.query.morelikethis.{ FieldsToUseForSimilarity, MoreLikeThisParams }
import com.github.seratch.scalikesolr.{ SolrDocument, Solr }
import com.github.seratch.scalikesolr.request.query.facet.{ Param, Value, FacetParam, FacetParams }
import com.github.seratch.scalikesolr.request.query.group.{ AsMainResultWhenUsingSimpleFormat, GroupFormat, GroupField, GroupParams }
import com.github.seratch.scalikesolr.request.{ UpdateRequest, AddRequest, QueryRequest }
import com.github.seratch.scalikesolr.request.query.{ Sort, Query }
import com.github.seratch.scalikesolr.util.Log
import runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ FlatSpec, FunSuite, Assertions }
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class SolrClient_doQueryStandardResponseSpec extends FlatSpec with ShouldMatchers {

  behavior of "SolrClient#doQuery StandardResponse"

  val log = new Log(LoggerFactory.getLogger(classOf[SolrClient_doQueryJavabinResponseSpec]))
  val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient()

  it should "be parepared" in {
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
       "pages_i" : 384,
       "timestamp" : "2006-03-21T13:40:15.518Z"
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
        "pages_i" : 304,
        "timestamp" : "2006-03-21T13:40:15.518Z"
      }
    """
    )
    request.documents = List(doc1, doc2)
    client.doAddDocuments(request)
    client.doCommit(new UpdateRequest())
  }

  it should "be available" in {
    val request = new QueryRequest(
      writerType = WriterType.Standard,
      query = Query("id:978-1423103349"))
    val response = client.doQuery(request)
    log.debug(response.toString)

    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)

    log.debug("-----------------------------")
    log.debug(response.response.documents.toString)
    response.response.documents foreach {
      case doc => {
        log.debug(doc.get("id").toString()) // "978-1423103349"
        log.debug(doc.get("cat").toListOrElse(Nil).toString) // List(book, hardcover)
        log.debug(doc.get("title").toString()) // "The Lightning Thief"
        log.debug(doc.get("pages_i").toIntOrElse(0).toString) // 384
        log.debug(doc.get("price").toDoubleOrElse(0.0).toString) // 12.5
        log.debug(doc.get("timestamp").toDateOrElse(null).toString) // 12.5
        assert(doc.get("id") != null)
        assert(doc.get("cat") != null)
        assert(doc.get("title") != null)
        assert(doc.get("pages_i") != null)
        assert(doc.get("price") != null)
        assert(doc.get("timestamp") != null)
      }
    }
    assert(response.response.documents.size == 1)
  }

  it should "be available with multibyte queries" in {
    val request = new QueryRequest(
      writerType = WriterType.Standard,
      query = Query("author:日本人"))
    val response = client.doQuery(request)
    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.responseHeader.params != null)
    log.debug(response.toString)
  }

  "Group params" should "be available" in {
    val request = new QueryRequest(
      writerType = WriterType.Standard,
      query = Query("genre_s:fantasy")
    )
    request.group = GroupParams(enabled = true, field = GroupField("author_t"))
    request.sort = Sort("page_i desc")
    val response = client.doQuery(request)
    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    log.debug(response.toString)
    log.debug(response.groups.toString)
    response.groups.groups foreach {
      case group => {
        log.debug(group.groupValue + " -> " + group.documents.toString)
        assert(group.groupValue != null)
        assert(group.documents.toString != null)
      }
    }
    assert(response.groups.groups.apply(0).numFound > 0)
    assert(response.groups.groups.apply(0).start == 0)
    assert(response.groups.groups.apply(1).numFound > 0)
    assert(response.groups.groups.apply(1).start == 0)
    assert(response.groups.groups.apply(2).numFound > 0)
    assert(response.groups.groups.apply(2).start == 0)
  }

  "Group params" should "be available with simple format" in {
    val request = new QueryRequest(
      writerType = WriterType.Standard,
      query = Query("genre_s:fantasy")
    )
    request.group = GroupParams(enabled = true, field = GroupField("author_t"), format = GroupFormat("simple"))
    request.sort = Sort("page_i desc")
    val response = client.doQuery(request)
    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    log.debug(response.toString)
    log.debug(response.groups.toString)
    response.groups.groups foreach {
      case group => {
        log.debug(group.groupValue + " -> " + group.documents.toString)
      }
    }
    assert(response.groups.groups.size == 1)
    assert(response.groups.groups.apply(0).numFound > 0)
    assert(response.groups.groups.apply(0).start == 0)
  }

  "Group params" should "be available with simple format, main" in {
    val request = new QueryRequest(
      writerType = WriterType.Standard,
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
    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.response.documents.size == 3)
    log.debug(response.toString)
    log.debug(response.groups.toString)
    response.response.documents foreach {
      case doc => log.debug(doc.toString)
    }
    assert(response.groups.groups.size == 0)
  }

  "Highlighting params" should "be available" in {
    val request = new QueryRequest(
      writerType = WriterType.Standard,
      query = Query("author:Rick"),
      sort = Sort("page_i desc")
    )
    request.highlighting = HighlightingParams(true)
    val response = client.doQuery(request)
    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.response.documents.size == 10)
    response.response.documents foreach {
      doc => log.debug(response.highlightings.get(doc.get("id").toString).toString)
    }
    assert(response.highlightings.size == 10)
    log.debug(response.toString)
    log.debug(response.highlightings.toString)
    response.highlightings.keys() foreach {
      case key => {
        val value = response.highlightings.get(key).get("author").toString
        assert(!value.startsWith("["))
        log.debug(key + "->" + value)
      }
    }
  }

  "MoreLikeThis params" should "be available" in {
    val request = new QueryRequest(
      writerType = WriterType.Standard,
      query = Query("author:Rick")
    )
    request.moreLikeThis = MoreLikeThisParams(true, 3, FieldsToUseForSimilarity("body"))
    val response = client.doQuery(request)
    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.response.documents.size == 10)
    log.debug(response.toString)
    log.debug(response.moreLikeThis.toString)
    response.response.documents foreach {
      doc =>
        {
          val id = doc.get("id").toString
          log.debug(id + "->" + response.moreLikeThis.getList(id).toString)
        }
    }
  }

  "Facet params" should "be available" in {
    val request = new QueryRequest(
      writerType = WriterType.Standard,
      query = Query("author:Rick")
    )
    request.facet = new FacetParams(enabled = true,
      params = List(new FacetParam(Param("facet.field"), Value("title")))
    )
    val response = client.doQuery(request)
    assert(response.responseHeader != null)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.response.documents.size == 10)
    log.debug(response.toString)
    log.debug("facetFields:" + response.facet.facetFields.toString)
    // Solr 3.2: Map(title -> SolrDocument(WriterType(standard),,Map(thief -> 1, sea -> 1, monster -> 1, lightn -> 1)))
    // Solr 3.3: Map(title -> SolrDocument(WriterType(standard),,Map(sea -> 1, thief -> 1, monsters -> 1, lightning -> 1, of -> 1, the -> 2)))
    assert(response.facet.facetFields.keys.size == 1)
    // Solr 3.2: response.facet.facetFields.get("title").get.keys.size must beEqual(4)
    // Solr 3.3: response.facet.facetFields.get("title").get.keys.size must beEqual(6)
    assert(response.facet.facetFields.get("title").get.keys.size >= 4)
  }

}