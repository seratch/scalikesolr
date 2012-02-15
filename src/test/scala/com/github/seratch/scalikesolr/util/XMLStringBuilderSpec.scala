package com.github.seratch.scalikesolr.util

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class XMLStringBuilderSpec extends FlatSpec with ShouldMatchers {

  behavior of "XMLStringBuilder"

  it should "be available" in {
    val sb = new XMLStringBuilder()
    sb should not be null
  }

}
