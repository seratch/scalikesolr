package com.github.seratch.scalikesolr.client

import java.net.URL
import com.github.seratch.scalikesolr.Solr
import org.slf4j.LoggerFactory
import org.junit._
import org.scalatest.Assertions
import com.github.seratch.scalikesolr.request.common.WriterType
import com.github.seratch.scalikesolr.request.UpdateRequest

class SolrClient_doUpdateTest extends Assertions {

  val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.SolrClientSpec")
  val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient()

  @Test
  def doUpdateInXMLIsAvailable() {
    val request = new UpdateRequest(
      requestBody = "<optimize/>"
    )
    val response = client.doUpdateInXML(request)
    assert(response != null)
  }

  @Test
  def doUpdateInJSONIsAvailable() {
    val request = new UpdateRequest(
      writerType = WriterType.JSON,
      requestBody = "{ \"optimize\": { \"waitFlush\":false, \"waitSearcher\":false } }"
    )
    val response = client.doUpdateInJSON(request)
    assert(response != null)
  }


}