package com.github.seratch.scalikesolr.request.query

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MaximumRowsReturnedSpec extends FlatSpec with ShouldMatchers {

  behavior of "MaximumRowsReturned"

  it should "be available" in {
    val rows: Int = 0
    val instance = new MaximumRowsReturned(rows)
    instance should not be null
  }

}
