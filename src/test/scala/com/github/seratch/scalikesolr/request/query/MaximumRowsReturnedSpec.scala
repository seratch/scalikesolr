package com.github.seratch.scalikesolr.request.query

import org.scalatest._
import org.scalatest.matchers._

class MaximumRowsReturnedSpec extends FlatSpec with ShouldMatchers {

  behavior of "MaximumRowsReturned"

  it should "be available" in {
    val rows: Int = 0
    val instance = new MaximumRowsReturned(rows)
    instance should not be null
    instance.getKey() should equal("rows")
  }

}
