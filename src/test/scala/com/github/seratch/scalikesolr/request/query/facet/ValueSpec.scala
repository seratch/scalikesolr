package com.github.seratch.scalikesolr.request.query.facet

import org.scalatest._
import org.scalatest.matchers._

class ValueSpec extends FlatSpec with ShouldMatchers {

  behavior of "Value"

  it should "be available" in {
    val value: String = ""
    val instance = new Value(value)
    instance should not be null
  }

}
