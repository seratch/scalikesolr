package com.github.seratch.scalikesolr.request.query.highlighting

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class RegexFragmenterSlopSpec extends FlatSpec with ShouldMatchers {

  behavior of "NumOfSnippets"

  it should "be available" in {
    val regexSlop: Double = 0D
    val instance = new RegexFragmenterSlop(regexSlop)
    instance should not be null
  }

}
