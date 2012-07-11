package com.github.seratch.scalikesolr.request.query

import org.scalatest._
import org.scalatest.matchers._

class IsOmitHeaderEnabledSpec extends FlatSpec with ShouldMatchers {

  behavior of "IsOmitHeaderEnabled"

  it should "be available" in {
    val omitHeader: Boolean = false
    val instance = new IsOmitHeaderEnabled(omitHeader)
    instance should not be null
    instance.getKey() should equal("omitHeader")
  }

}
