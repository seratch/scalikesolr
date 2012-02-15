package com.github.seratch.scalikesolr.response

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import com.github.seratch.scalikesolr.request.common.WriterType

@RunWith(classOf[JUnitRunner])
class PingResponseSpec extends FlatSpec with ShouldMatchers {

  behavior of "PingResponse"

  it should "be available" in {
    val writerType: WriterType = WriterType.Standard
    val rawBody: String = """
      """
    val instance = new PingResponse(writerType, rawBody)
    instance should not be null
  }

}
