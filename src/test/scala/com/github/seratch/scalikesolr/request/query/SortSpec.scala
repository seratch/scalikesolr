package com.github.seratch.scalikesolr.request.query

import org.scalatest._
import org.scalatest.matchers._

class SortSpec extends FlatSpec with ShouldMatchers {

  behavior of "Sort"

  it should "be available" in {
    val sort: String = ""
    val instance = new Sort(sort)
    instance should not be null
    instance.getKey() should equal("sort")
  }

}
