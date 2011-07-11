package com.github.seratch.scalikesolr.client

import java.net.URL
import com.github.seratch.scalikesolr.Solr
import org.slf4j.LoggerFactory
import org.junit._
import org.scalatest.Assertions
import com.github.seratch.scalikesolr.request.PingRequest
import com.github.seratch.scalikesolr.request.common.WriterType

class SolrClient_doPingTest extends Assertions {

  val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.SolrClientSpec")
  val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient()

  @Test
  def available() {
    val request = new PingRequest()
    val response = client.doPing(request)
    log.debug(response.toString)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.status == "OK")
  }

  @Test
  def availableInJSON() {
    val request = new PingRequest(writerType = WriterType.JSON)
    val response = client.doPing(request)
    log.debug(response.toString)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    assert(response.status == "OK")
  }


}