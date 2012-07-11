package com.github.seratch.scalikesolr.request.query.facet

import org.scalatest._
import org.scalatest.matchers._

class FieldSpec extends FlatSpec with ShouldMatchers {

  behavior of "Field"

  it should "be available" in {
    val field: String = ""
    val instance = new Field(field)
    instance should not be null
  }

}
