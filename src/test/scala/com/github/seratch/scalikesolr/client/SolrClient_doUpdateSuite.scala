package com.github.seratch.scalikesolr.client

import com.github.seratch.scalikesolr.Solr
import org.slf4j.LoggerFactory
import org.junit._
import com.github.seratch.scalikesolr.request.common.WriterType
import com.github.seratch.scalikesolr.request.{QueryRequest, UpdateRequest}
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import runner.RunWith
import java.net.{SocketTimeoutException, URL}

@RunWith(classOf[JUnitRunner])
class SolrClient_doUpdateSuite extends FunSuite {

  type ? = this.type

  val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.SolrClientSpec")
  val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient()

  test("doUpdateInXMLIsAvailable") {
    val request = new UpdateRequest(
      requestBody = "<optimize/>"
    )
    val response = client.doUpdateInXML(request)
    assert(response != null)
  }

  test("doUpdateInJSONIsAvailable") {
    val request = new UpdateRequest(
      writerType = WriterType.JSON,
      requestBody = "{ \"optimize\": { \"waitFlush\":false, \"waitSearcher\":false } }"
    )
    val response = client.doUpdateInJSON(request)
    assert(response != null)
  }

  test("tooSmallConnectTimeoutValue") {
    val client = Solr.httpServer(new URL("http://localhost:9999/solr")).newClient(
      connectTimeout = 1,
      readTimeout = 10000
    )
    intercept[SocketTimeoutException] {
      client.doOptimize(new UpdateRequest)
    }
  }

  test("tooSmallReadTimeoutValue") {
    val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient(
      connectTimeout = 1000,
      readTimeout = 1
    )
    intercept[SocketTimeoutException] {
      client.doOptimize(new UpdateRequest)
    }
  }

}