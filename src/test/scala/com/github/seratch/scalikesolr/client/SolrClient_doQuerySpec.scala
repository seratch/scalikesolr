package com.github.seratch.scalikesolr.client

import java.net.URL
import org.slf4j.LoggerFactory
import com.github.seratch.scalikesolr.request.common.WriterType
import com.github.seratch.scalikesolr.request.query.highlighting.HighlightingParams
import com.github.seratch.scalikesolr.request.query.morelikethis.{ FieldsToUseForSimilarity, MoreLikeThisParams }
import com.github.seratch.scalikesolr.{ SolrDocumentValue, SolrDocument, Solr }
import com.github.seratch.scalikesolr.request.query.facet.{ Param, Value, FacetParam, FacetParams }
import com.github.seratch.scalikesolr.request.query.group.{ AsMainResultWhenUsingSimpleFormat, GroupFormat, GroupField, GroupParams }
import com.github.seratch.scalikesolr.request.{ UpdateRequest, QueryRequest }
import com.github.seratch.scalikesolr.request.query.{ Sort, Query, QueryType }
import com.github.seratch.scalikesolr.response.query.Group
import com.github.seratch.scalikesolr.util.Log
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class SolrClient_doQuerySpec extends FlatSpec with ShouldMatchers with testutil.PreparingDocuments {

  behavior of "SolrClient#doQuery"

  val log = new Log(LoggerFactory.getLogger(classOf[SolrClient_doQuerySpec]))
  val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient()

  it should "be available" in {
    val request = QueryRequest(
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
        doc.get("cat").toListOrElse(Nil).toString should equal("List(book,  paperback)")
        doc.get("title").toString should equal("The Sea of Monsters")
        doc.get("pages_i").toString should equal("304")
        doc.get("price").toString should equal("6.49")
        doc.get("timestamp").toString should equal("2006-03-21T13:40:15.518Z")
      }
    }
  }

  it should "be available with multibyte queries" in {
    val request = QueryRequest(
      query = Query("author:日本人"))
    val response = client.doQuery(request)
    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be >= 0
    response.responseHeader.params should not be null
    log.debug(response.toString)
  }

  it should "resolve requestHandler" in {
    val request = QueryRequest(
      query = Query("id:978-1423103349"))
    request.queryType = QueryType("query")
    request.writerType = WriterType.JavaBinary
    val response = client.doQuery(request)

    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be >= 0
    response.responseHeader.params.get("qt").toString should equal("")

    response.response.documents.size should equal(1)
  }

  "Group params" should "be available" in {
    val request = QueryRequest(
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
  }

  "Group params" should "be available with simple format" in {
    val request = QueryRequest(
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
        group.documents.size should be > 0
        group.documents.toString should not be null
      }
    }
    response.groups.groups(0).numFound should be > 0
    response.groups.groups(0).start should equal(0)
  }

  "Group params" should "be available with simple format, main" in {
    val request = QueryRequest(
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

    response.response.documents.size should equal(1)
    response.response.documents foreach {
      case doc: SolrDocument =>
        doc.writerType should equal(WriterType.Standard)
        doc.get("author").toString should not be null
    }
    response.groups.groups.size should equal(0)
  }

  "Highlighting params" should "be available" in {
    val request = QueryRequest(
      query = Query("author:Rick"),
      sort = Sort("page_i desc")
    )
    request.highlighting = HighlightingParams(true)
    val response = client.doQuery(request)

    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be >= 0
    response.response.documents.size should equal(response.highlightings.size)
    response.response.documents foreach {
      doc: SolrDocument =>
        doc.writerType should equal(WriterType.Standard)
        doc.get("id").toString should not be null
    }
    response.highlightings.keys foreach {
      case key => {
        val value = response.highlightings.get(key).get("author").toString
        value should include regex "<em>"
      }
    }
  }

  "MoreLikeThis params" should "be available" in {
    val request = QueryRequest(
      query = Query("author:Rick")
    )
    request.moreLikeThis = MoreLikeThisParams(true, 3, FieldsToUseForSimilarity("body"))

    val response = client.doQuery(request)

    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be >= 0
    response.response.documents.size should be > 0

    response.response.documents foreach {
      doc: SolrDocument =>
        val id = doc.get("id").toString
        response.moreLikeThis.getList(id).toString should not be null
    }
  }

  "Facet params" should "be available" in {
    val request = QueryRequest(
      query = Query("author:Rick")
    )
    request.facet = new FacetParams(enabled = true,
      params = List(new FacetParam(Param("facet.field"), Value("title")))
    )

    val response = client.doQuery(request)

    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be >= 0
    response.response.documents.size should be > 0

    response.facet.facetFields.get("title") match {
      case Some(doc: SolrDocument) =>
        doc.writerType should equal(WriterType.Standard)
        doc.get("sea").toIntOrElse(-1) should equal(1)
      case _ => fail("facet field not found")
    }
    response.facet.facetFields.get("title").get.keys.size should be >= 4
  }

}
