package com.github.seratch.scalikesolr.client

import java.net.URL
import com.github.seratch.scalikesolr.Solr
import org.slf4j.LoggerFactory
import org.junit._
import com.github.seratch.scalikesolr.request.{DeleteRequest, UpdateRequest}
import runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FunSuite, Assertions}

@RunWith(classOf[JUnitRunner])
class SolrClient_doDeleteDocumentsSuite extends FunSuite {

  type ? = this.type

  val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.SolrClientSpec")
  val client = Solr.httpServer(new URL("http://localhost:8983/solr")).newClient()

  test("available") {
    val request = new DeleteRequest(uniqueKeysToDelete = List("978-0641723445"))
    val response = client.doDeleteDocuments(request)
    //    log.debug(response.toString)
    assert(response.responseHeader.status >= 0)
    assert(response.responseHeader.qTime >= 0)
    client.doCommit(new UpdateRequest())
  }

}