package com.github.seratch.scalikesolr.request

import org.scalatest._
import org.scalatest.matchers._
import common.WriterType
import com.github.seratch.scalikesolr.SolrCore

class DIHCommandRequestSpec extends FlatSpec with ShouldMatchers {

  behavior of "DIHCommandRequest"

  it should "be available" in {
    val core: SolrCore = null
    val writerType: WriterType = null
    val command: String = ""
    val instance = new DIHCommandRequest(core, writerType, command)
    instance should not be null
  }

}
