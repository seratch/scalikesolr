package com.github.seratch.scalikesolr.client

import java.net.URL
import org.slf4j.LoggerFactory
import com.github.seratch.scalikesolr.request.common.WriterType
import com.github.seratch.scalikesolr.request.query.highlighting.HighlightingParams
import com.github.seratch.scalikesolr.request.query.morelikethis.{ FieldsToUseForSimilarity, MoreLikeThisParams }
import com.github.seratch.scalikesolr.{ SolrDocument, Solr }
import com.github.seratch.scalikesolr.request.query.facet.{ Param, Value, FacetParam, FacetParams }
import com.github.seratch.scalikesolr.request.query.group.{ AsMainResultWhenUsingSimpleFormat, GroupFormat, GroupField, GroupParams }
import com.github.seratch.scalikesolr.request.{ UpdateRequest, AddRequest, QueryRequest }
import com.github.seratch.scalikesolr.request.query.{ Sort, Query, FilterQuery }
import com.github.seratch.scalikesolr.response.query.Group
import com.github.seratch.scalikesolr.util.Log
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class SolrClient_doQueryJavabinResponseSpec extends FlatSpec with ShouldMatchers {

  behavior of "SolrClient#doQuery JavabinResponse"

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
      writerType = WriterType.JavaBinary,
      query = Query("id:978-1423103349"))
    val response = client.doQuery(request)
    log.debug(response.toString)

    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be >= 0

    log.debug(response.response.documents.toString)
    response.response.documents.size should equal(1)
    response.response.documents foreach {
      case doc: SolrDocument => {
        doc.get("id").toString should equal("978-1423103349")
        doc.get("cat").toListOrElse(Nil).toString should equal("List([book,  paperback])")
        doc.get("title").toString should equal("[The Sea of Monsters]")
        doc.get("pages_i").toString should equal("304")
        doc.get("price").toString should equal("6.49")
        doc.get("timestamp").toString should equal("Tue Mar 21 22:40:15 JST 2006")
      }
    }
  }

  it should "be available with multibyte queries" in {
    val request = new QueryRequest(
      writerType = WriterType.JavaBinary,
      query = Query("author:日本人"))
    val response = client.doQuery(request)
    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be >= 0
    response.responseHeader.params should not be null
    log.debug(response.toString)
  }

  "Group params" should "be available" in {
    val request = new QueryRequest(
      writerType = WriterType.JavaBinary,
      query = Query("genre_s:fantasy")
    )
    request.group = GroupParams(enabled = true, field = GroupField("author_t"))
    request.sort = Sort("page_i desc")
    val response = client.doQuery(request)
    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be >= 0
    response.responseHeader.params should not be null

    response.groups.groups.size should be > 0
    response.groups.groups foreach {
      case group: Group => {
        group.groupValue should not be null
        group.documents.toString should not be null
      }
    }
    response.groups.groups(0).numFound should be > 0
    response.groups.groups(0).start should equal(0)
    response.groups.groups(1).numFound should be > 0
    response.groups.groups(1).start should equal(0)
    response.groups.groups(2).numFound should be > 0
    response.groups.groups(2).start should equal(0)
  }

  "Group params" should "be available with simple format" in {
    val request = new QueryRequest(
      writerType = WriterType.JavaBinary,
      query = Query("genre_s:fantasy")
    )
    request.group = GroupParams(enabled = true, field = GroupField("author_t"), format = GroupFormat("simple"))
    request.sort = Sort("page_i desc")
    val response = client.doQuery(request)

    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be >= 0

    response.groups.groups.size should equal(1)
    response.groups.groups foreach {
      case group => {
        group.groupValue should equal(null)
        group.documents.size should be > 0
        group.documents.toString should not be null
      }
    }
    response.groups.groups(0).numFound should be > 0
    response.groups.groups(0).start should equal(0)
  }

  "Group params" should "be available with simple format, main" in {
    val request = new QueryRequest(
      writerType = WriterType.JavaBinary,
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

    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be >= 0

    response.response.documents.size should equal(3)
    response.response.documents foreach {
      case doc: SolrDocument =>
        doc.writerType should equal(WriterType.JavaBinary)
        doc.get("author").toString should not be null
    }
    response.groups.groups.size should equal(0)
  }

  "Highlighting params" should "be available" in {
    val request = new QueryRequest(
      writerType = WriterType.JavaBinary,
      query = Query("author:Rick"),
      sort = Sort("page_i desc")
    )
    request.highlighting = HighlightingParams(true)
    val response = client.doQuery(request)

    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be >= 0
    response.response.documents.size should equal(2)
    response.response.documents foreach {
      doc: SolrDocument =>
        doc.writerType should equal(WriterType.JavaBinary)
        doc.get("id").toString should not be null
    }
    response.highlightings.size should equal(2)
    response.highlightings.keys foreach {
      case key => {
        val value = response.highlightings.get(key).get("author").toString
        value should include regex "<em>"
      }
    }
  }

  "MoreLikeThis params" should "be available" in {
    val request = new QueryRequest(
      writerType = WriterType.JavaBinary,
      query = Query("author:Rick")
    )
    request.moreLikeThis = MoreLikeThisParams(true, 3, FieldsToUseForSimilarity("body"))

    val response = client.doQuery(request)

    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be >= 0
    response.response.documents.size should equal(2)

    response.response.documents foreach {
      doc: SolrDocument =>
        val id = doc.get("id").toString
        response.moreLikeThis.getList(id).toString should not be null
    }
  }

  "Facet params" should "be available" in {
    val request = new QueryRequest(
      writerType = WriterType.JavaBinary,
      query = Query("author:Rick")
    )
    request.facet = new FacetParams(enabled = true,
      params = List(new FacetParam(Param("facet.field"), Value("title")))
    )

    val response = client.doQuery(request)

    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be >= 0
    response.response.documents.size should equal(2)

    response.facet.facetFields.get("title") match {
      case Some(doc: SolrDocument) =>
        doc.writerType should equal(WriterType.JavaBinary)
        doc.get("sea").toIntOrElse(-1) should equal(1)
      case _ => fail("facet field not found")
    }
    response.facet.facetFields.get("title").get.keys.size should be >= 4
  }

  "single FilterQuery" should "be available" in {
    val request = new QueryRequest(
      writerType = WriterType.JavaBinary,
      query = Query("name:A Game of Thrones"),
      filterQuery = FilterQuery(fq = "+name:of +id:0553573403")
    )
    val response = client.doQuery(request)
    response.response.documents.size should equal(1)
  }

  "multiple FilterQuery" should "be available" in {
    val request = new QueryRequest(
      writerType = WriterType.JavaBinary,
      query = Query("name:A Game of Thrones"),
      filterQuery = FilterQuery(multiple = Seq("name:of", "id:0553573403"))
    )
    val response = client.doQuery(request)
    response.response.documents.size should equal(1)
  }

}
