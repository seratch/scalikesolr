package com.github.seratch.scalikesolr.response

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import com.github.seratch.scalikesolr.request.common.WriterType
import com.github.seratch.scalikesolr._

@RunWith(classOf[JUnitRunner])
class DIHCommandResponseSpec extends FlatSpec with ShouldMatchers {

  behavior of "DIHCommandResponse"

  it should "be available" in {
    val writerType: WriterType = WriterType.Standard
    val rawBody: String = """
      """
    val instance = new DIHCommandResponse(writerType, rawBody)
    instance should not be null
  }

}
