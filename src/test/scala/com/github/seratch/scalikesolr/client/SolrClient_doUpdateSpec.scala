package com.github.seratch.scalikesolr.client

import com.github.seratch.scalikesolr.Solr
import org.slf4j.LoggerFactory
import com.github.seratch.scalikesolr.request.common.WriterType
import com.github.seratch.scalikesolr.request.UpdateRequest
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import java.net.{ ConnectException, SocketTimeoutException, URL }
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class SolrClient_doUpdateSpec extends FlatSpec with ShouldMatchers {

  behavior of "SolrClient#doUpdate"

  val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.SolrClientSpec")
  val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient()

  "doUpdate in XML" should "be available" in {
    val request = new UpdateRequest(
      requestBody = "<optimize/>"
    )
    val response = client.doUpdateInXML(request)
    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be > 0
    response.rawBody should fullyMatch regex """<\?xml version="1.0" encoding="UTF-8"\?>
      |<response>
      |<lst name="responseHeader"><int name="status">0</int><int name="QTime">\d+</int></lst>
      |</response>
      |""".stripMargin
  }

  "doUpdate in JSON" should "be available" in {
    val request = new UpdateRequest(
      writerType = WriterType.JSON,
      requestBody = "{ \"optimize\": { \"waitFlush\":false, \"waitSearcher\":false } }"
    )
    val response = client.doUpdateInJSON(request)
    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be > 0
    response.rawBody should fullyMatch regex """\{"responseHeader":\{"status":0,"QTime":\d+\}\}
      |""".stripMargin
  }

  it should "throw SocketTimeoutException when specifying too small connect timeout" in {
    val client = Solr.httpServer(new URL("http://localhost:9999/solr")).newClient(
      connectTimeout = 1,
      readTimeout = 10000
    )
    intercept[ConnectException] {
      client.doOptimize(new UpdateRequest)
    }
  }

  it should "throw SocketTimeoutException when specifying too small read timeout" in {
    val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient(
      connectTimeout = 1000,
      readTimeout = 1
    )
    intercept[SocketTimeoutException] {
      client.doOptimize(new UpdateRequest)
    }
  }

}
