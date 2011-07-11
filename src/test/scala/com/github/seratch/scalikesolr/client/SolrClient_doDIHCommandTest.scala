package com.github.seratch.scalikesolr.client

import java.net.URL
import com.github.seratch.scalikesolr.Solr
import org.slf4j.LoggerFactory
import org.junit._
import org.scalatest.Assertions
import com.github.seratch.scalikesolr.request.DIHCommandRequest

class SolrClient_doDIHCommandTest extends Assertions {

  val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.SolrClientSpec")
  val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient()

  @Test
  @Ignore
  def available() {
    val request = new DIHCommandRequest(command = "delta-import")
    val response = client.doDIHCommand(request)
    log.debug(response.toString)
  }

}