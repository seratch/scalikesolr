package com.github.seratch.scalikesolr.request.query.highlighting

import org.scalatest._
import org.scalatest.matchers._

class AlternateFieldSpec extends FlatSpec with ShouldMatchers {

  behavior of "AlternateField"

  it should "be available" in {
    val alternateField: String = "foo"
    val instance = new AlternateField(alternateField)
    instance should not be null
    instance.getKey() should equal("hl.alternateField")
    instance.getValue() should equal("foo")
  }

}
