package com.github.seratch.scalikesolr.request.common

import org.scalatest._
import org.scalatest.matchers._

class WriterTypeSpec extends FlatSpec with ShouldMatchers {

  behavior of "WriterType"

  it should "be available" in {
    val wt = new WriterType("standard")
    wt should not be null
    wt should equal(WriterType.Standard)
  }

}
