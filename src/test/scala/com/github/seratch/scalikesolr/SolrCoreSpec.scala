package com.github.seratch.scalikesolr

import org.scalatest._
import org.scalatest.matchers._

class SolrCoreSpec extends FlatSpec with ShouldMatchers {

  behavior of "SolrCore"

  it should "be available" in {
    val name: String = "core"
    val core = new SolrCore(name)
    core should not be null
  }

}
