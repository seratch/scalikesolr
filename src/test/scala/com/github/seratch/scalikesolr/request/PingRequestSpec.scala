package com.github.seratch.scalikesolr.request

import org.scalatest._
import org.scalatest.matchers._
import common.WriterType
import com.github.seratch.scalikesolr.SolrCore

class PingRequestSpec extends FlatSpec with ShouldMatchers {

  behavior of "PingRequest"

  it should "be available" in {
    val core: SolrCore = null
    val writerType: WriterType = null
    val instance = new PingRequest(core, writerType)
    instance should not be null
  }

}
