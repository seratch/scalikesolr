package com.github.seratch.scalikesolr

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.net.URL

@RunWith(classOf[JUnitRunner])
class SolrClientSpec extends FlatSpec with ShouldMatchers {

  behavior of "SolrClient"

  it should "be available" in {
    val client: SolrClient = new HttpSolrClient(new URL("http://localhost:8983/solr"))
    client should not be null
  }

}
