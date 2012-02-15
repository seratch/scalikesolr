package com.github.seratch.scalikesolr

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.net.URL

@RunWith(classOf[JUnitRunner])
class HttpSolrServerSpec extends FlatSpec with ShouldMatchers {

  behavior of "HttpSolrServer"

  it should "be available" in {
    val url: URL = new URL("http://localhost:8983/solr")
    val server = new HttpSolrServer(url)
    server should not be null
  }

}
