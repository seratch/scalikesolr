package com.github.seratch.scalikesolr.request.query.highlighting

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FieldsHighlightedSpec extends FlatSpec with ShouldMatchers {

  behavior of "FieldsHighlighted"

  it should "be available" in {
    val fl: String = "bar"
    val instance = new FieldsHighlighted(fl)
    instance should not be null
    instance.getKey() should equal("hl.fl")
    instance.getValue() should equal("bar")
  }

}
