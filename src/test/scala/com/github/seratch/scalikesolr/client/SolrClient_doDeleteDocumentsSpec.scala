package com.github.seratch.scalikesolr.client

import java.net.URL
import com.github.seratch.scalikesolr.Solr
import org.slf4j.LoggerFactory
import com.github.seratch.scalikesolr.request.{ DeleteRequest, UpdateRequest }
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class SolrClient_doDeleteDocumentsSpec extends FlatSpec with ShouldMatchers {

  behavior of "SolrClient#doDeleteDocuments"

  val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.SolrClientSpec")
  val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient()

  it should "be available" in {
    val request = new DeleteRequest(uniqueKeysToDelete = List("978-0641723445"))
    val response = client.doDeleteDocuments(request)

    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be >= 0
    response.rawBody should fullyMatch regex """<\?xml version="1.0" encoding="UTF-8"\?>
      |<response>
      |<lst name="responseHeader"><int name="status">0</int><int name="QTime">\d+</int></lst>
      |</response>
      |""".stripMargin

    client.doCommit(new UpdateRequest())
  }

}
