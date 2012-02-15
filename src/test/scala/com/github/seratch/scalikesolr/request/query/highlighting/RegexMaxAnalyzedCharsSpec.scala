package com.github.seratch.scalikesolr.request.query.highlighting

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class RegexMaxAnalyzedCharsSpec extends FlatSpec with ShouldMatchers {

  behavior of "RegexMaxAnalyzedChars"

  it should "be available" in {
    val regexMaxAnalyzedChars: Int = 0
    val instance = new RegexMaxAnalyzedChars(regexMaxAnalyzedChars)
    instance should not be null
    instance.getKey() should equal("hl.regex.maxAnalyzedChars")
  }

}
