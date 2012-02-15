package com.github.seratch.scalikesolr.request.query.highlighting

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class AlternateFieldSpec extends FlatSpec with ShouldMatchers {

  behavior of "AlternateField"

  it should "be available" in {
    val alternateField: String = ""
    val instance = new AlternateField(alternateField)
    instance should not be null
  }

}
