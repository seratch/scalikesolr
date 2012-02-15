package com.github.seratch.scalikesolr.request.query

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class IsOmitHeaderEnabledSpec extends FlatSpec with ShouldMatchers {

  behavior of "IsOmitHeaderEnabled"

  it should "be available" in {
    val omitHeader: Boolean = false
    val instance = new IsOmitHeaderEnabled(omitHeader)
    instance should not be null
  }

}
