package com.github.seratch.scalikesolr.client

import java.net.URL
import com.github.seratch.scalikesolr.Solr
import org.slf4j.LoggerFactory
import org.junit._
import com.github.seratch.scalikesolr.request.UpdateRequest
import runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ FlatSpec, FunSuite, Assertions }
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class SolrClient_doCommitSpec extends FlatSpec with ShouldMatchers {

  behavior of "SolrClient#doCommit"

  val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.SolrClientSpec")
  val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient()

  it should "be available" in {
    val request = new UpdateRequest()
    val response = client.doCommit(request)
    //    log.debug(response.toString)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
  }

}