package com.github.seratch.scalikesolr.client

import java.net.URL
import com.github.seratch.scalikesolr.Solr
import org.slf4j.LoggerFactory
import com.github.seratch.scalikesolr.request.{ DeleteRequest, UpdateRequest }
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import com.github.seratch.scalikesolr.request.query.Query

class SolrClient_doDeleteDocumentsSpec extends FlatSpec with ShouldMatchers {

  behavior of "SolrClient#doDeleteDocuments"

  val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.SolrClientSpec")
  val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient()

  it should "be available when specifying uniq keys to delete" in {
    val request = new DeleteRequest(uniqueKeysToDelete = List("978-0641723445"))
    val response = client.doDeleteDocuments(request)

    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be >= 0
    response.rawBody.replaceAll("\r", "").replaceAll("\n", "").trim should fullyMatch regex """<\?xml version="1.0" encoding="UTF-8"\?>
                                               |<response>
                                               |<lst name="responseHeader"><int name="status">0</int><int name="QTime">\d+</int></lst>
                                               |</response>
                                               | """.stripMargin.replaceAll("\r", "").replaceAll("\n", "").trim

    client.doCommit(new UpdateRequest())
  }

  it should "be available when specifying queries" in {
    val request = new DeleteRequest(queries = List(Query("something")))
    val response = client.doDeleteDocuments(request)

    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be >= 0
    response.rawBody.replaceAll("\r", "").replaceAll("\n", "").trim should fullyMatch regex """<\?xml version="1.0" encoding="UTF-8"\?>
                                               |<response>
                                               |<lst name="responseHeader"><int name="status">0</int><int name="QTime">\d+</int></lst>
                                               |</response>
                                               | """.stripMargin.replaceAll("\r", "").replaceAll("\n", "").trim

    client.doCommit(new UpdateRequest())
  }

}
