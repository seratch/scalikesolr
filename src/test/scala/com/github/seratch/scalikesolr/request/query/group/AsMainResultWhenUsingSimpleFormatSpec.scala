package com.github.seratch.scalikesolr.request.query.group

import org.scalatest._
import org.scalatest.matchers._

class AsMainResultWhenUsingSimpleFormatSpec extends FlatSpec with ShouldMatchers {

  behavior of "AsMainResultWhenUsingSimpleFormat"

  it should "be available" in {
    val main: Boolean = false
    val instance = new AsMainResultWhenUsingSimpleFormat(main)
    instance should not be null
    instance.getKey() should equal("group.main")
  }

}
