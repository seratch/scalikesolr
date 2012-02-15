package com.github.seratch.scalikesolr.request.query

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FieldsToReturnSpec extends FlatSpec with ShouldMatchers {

  behavior of "FieldsToReturn"

  it should "be available" in {
    val fl: String = ""
    val instance = new FieldsToReturn(fl)
    instance should not be null
  }

}
