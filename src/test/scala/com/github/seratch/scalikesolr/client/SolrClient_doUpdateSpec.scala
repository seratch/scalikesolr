package com.github.seratch.scalikesolr.client

import com.github.seratch.scalikesolr.Solr
import org.slf4j.LoggerFactory
import org.junit._
import com.github.seratch.scalikesolr.request.common.WriterType
import com.github.seratch.scalikesolr.request.{ QueryRequest, UpdateRequest }
import org.scalatest.junit.JUnitRunner
import runner.RunWith
import java.net.{ SocketTimeoutException, URL }
import org.scalatest.{ FlatSpec, FunSuite }
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
    assert(response != null)
  }

  "doUpdate in JSON" should "be available" in {
    val request = new UpdateRequest(
      writerType = WriterType.JSON,
      requestBody = "{ \"optimize\": { \"waitFlush\":false, \"waitSearcher\":false } }"
    )
    val response = client.doUpdateInJSON(request)
    assert(response != null)
  }

  it should "throw SocketTimeoutException when specifying too small connect timeout" in {
    val client = Solr.httpServer(new URL("http://localhost:9999/solr")).newClient(
      connectTimeout = 1,
      readTimeout = 10000
    )
    intercept[SocketTimeoutException] {
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