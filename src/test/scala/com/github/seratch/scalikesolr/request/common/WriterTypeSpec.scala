package com.github.seratch.scalikesolr.request.common

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class WriterTypeSpec extends FlatSpec with ShouldMatchers {

  behavior of "WriterType"

  it should "be available" in {
    val wt = new WriterType("json")
    wt should not be null
    wt should equal(WriterType.JSON)
  }

}
