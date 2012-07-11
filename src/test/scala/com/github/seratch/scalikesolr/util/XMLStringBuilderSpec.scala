package com.github.seratch.scalikesolr.util

import org.scalatest._
import org.scalatest.matchers._

class XMLStringBuilderSpec extends FlatSpec with ShouldMatchers {

  behavior of "XMLStringBuilder"

  it should "be available" in {
    val sb = new XMLStringBuilder()
    sb should not be null
  }

}
