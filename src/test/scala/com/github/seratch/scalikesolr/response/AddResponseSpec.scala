package com.github.seratch.scalikesolr.response

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import com.github.seratch.scalikesolr.request.common.WriterType

@RunWith(classOf[JUnitRunner])
class AddResponseSpec extends FlatSpec with ShouldMatchers {

  type ? = this.type // for IntelliJ IDEA

  "AddResponse" should "be available" in {
    val writerType: WriterType = null
    val rawBody: String = ""
    val instance = new AddResponse(writerType, rawBody)
    instance should not be null
  }

}
