package com.github.seratch.scalikesolr.request.query.group

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class AsMainResultWhenUsingSimpleFormatSpec extends FlatSpec with ShouldMatchers {

  behavior of "AsMainResultWhenUsingSimpleFormat"

  it should "be available" in {
    val main: Boolean = false
    val instance = new AsMainResultWhenUsingSimpleFormat(main)
    instance should not be null
  }

}
