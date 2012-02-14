package com.github.seratch.scalikesolr.client

import java.net.URL
import com.github.seratch.scalikesolr.Solr
import org.slf4j.LoggerFactory
import com.github.seratch.scalikesolr.request.DIHCommandRequest
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class SolrClient_doDIHCommandSpec extends FlatSpec with ShouldMatchers {

  behavior of "SolrClient#doDIHCommand"

  val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.SolrClientSpec")
  val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient()

  it should "be available" in {
    val request = new DIHCommandRequest(command = "delta-import")
    //    val response = client.doDIHCommand(request)
    //    log.debug(response.toString)
  }

}