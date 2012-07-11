package com.github.seratch.scalikesolr.request.query.highlighting

import org.scalatest._
import org.scalatest.matchers._

class MaxAlternateFieldLengthSpec extends FlatSpec with ShouldMatchers {

  behavior of "MaxAlternateFieldLength"

  it should "be available" in {
    val maxAlternateFieldLength: Int = 0
    val instance = new MaxAlternateFieldLength(maxAlternateFieldLength)
    instance should not be null
    instance.getKey() should equal("hl.maxAlternateFieldLength")
  }

}
