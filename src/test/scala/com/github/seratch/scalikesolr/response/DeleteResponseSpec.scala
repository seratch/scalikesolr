package com.github.seratch.scalikesolr.response

import org.scalatest._
import org.scalatest.matchers._
import com.github.seratch.scalikesolr.request.common.WriterType

class DeleteResponseSpec extends FlatSpec with ShouldMatchers {

  behavior of "DeleteResponse"

  it should "be available" in {
    val writerType: WriterType = WriterType.Standard
    val rawBody: String = """"
                          """
    val instance = new DeleteResponse(writerType, rawBody)
    instance should not be null
  }

}
