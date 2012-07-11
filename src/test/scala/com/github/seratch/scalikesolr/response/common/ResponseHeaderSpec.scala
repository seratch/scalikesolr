package com.github.seratch.scalikesolr.response.common

import org.scalatest._
import org.scalatest.matchers._
import com.github.seratch.scalikesolr.SolrDocument

class ResponseHeaderSpec extends FlatSpec with ShouldMatchers {

  behavior of "ResponseHeader"

  it should "be available" in {
    val status: Int = 0
    val qTime: Int = 0
    val params: SolrDocument = null
    val instance = new ResponseHeader(status, qTime, params)
    instance should not be null
  }

}
