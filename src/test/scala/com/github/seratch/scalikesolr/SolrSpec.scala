package com.github.seratch.scalikesolr

import org.scalatest._
import org.scalatest.matchers._

class SolrSpec extends FlatSpec with ShouldMatchers {

  behavior of "Solr"

  it should "be available" in {
    val solr = Solr
    solr should not be null
  }

}
