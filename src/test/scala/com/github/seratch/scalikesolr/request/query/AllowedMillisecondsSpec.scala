package com.github.seratch.scalikesolr.request.query

import org.scalatest._
import org.scalatest.matchers._

class AllowedMillisecondsSpec extends FlatSpec with ShouldMatchers {

  behavior of "TimeoutMilliseconds"

  it should "be available" in {
    val timeAllowed: Long = 0L
    val instance = new AllowedMilliseconds(timeAllowed)
    instance should not be null
    instance.getKey() should equal("timeAllowed")
  }

}
