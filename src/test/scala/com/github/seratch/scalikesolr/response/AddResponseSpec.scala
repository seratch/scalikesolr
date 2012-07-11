package com.github.seratch.scalikesolr.response

import org.scalatest._
import org.scalatest.matchers._
import com.github.seratch.scalikesolr.request.common.WriterType

class AddResponseSpec extends FlatSpec with ShouldMatchers {

  behavior of "AddResponse"

  "AddResponse" should "be available" in {
    val writerType: WriterType = null
    val rawBody: String = ""
    val instance = new AddResponse(writerType, rawBody)
    instance should not be null
  }

}
