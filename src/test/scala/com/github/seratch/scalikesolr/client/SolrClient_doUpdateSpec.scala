package com.github.seratch.scalikesolr.client

import com.github.seratch.scalikesolr.Solr
import org.slf4j.LoggerFactory
import com.github.seratch.scalikesolr.request.common.WriterType
import com.github.seratch.scalikesolr.request.UpdateRequest
import java.net.{ ConnectException, SocketTimeoutException, URL }
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

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
    response.rawBody.replaceAll("\r", "").replaceAll("\n", "").trim should fullyMatch regex """<\?xml version="1.0" encoding="UTF-8"\?>
                                               |<response>
                                               |<lst name="responseHeader"><int name="status">0</int><int name="QTime">\d+</int></lst>
                                               |</response>
                                               | """.stripMargin.replaceAll("\r", "").replaceAll("\n", "").trim
  }

  "doUpdate in JSON" should "be available" in {
    val request = new UpdateRequest(
      writerType = WriterType.JSON,
      requestBody = "{ \"optimize\": { \"waitSearcher\":false } }"
    )
    val response = client.doUpdateInJSON(request)
    response should not be null
    response.responseHeader.status should equal(0)
    response.responseHeader.qTime should be > 0
    response.rawBody.replaceAll("\r", "").replaceAll("\n", "").trim should fullyMatch regex """\{"responseHeader":\{"status":0,"QTime":\d+\}\}
                                               | """.stripMargin.replaceAll("\r", "").replaceAll("\n", "").trim
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

}
