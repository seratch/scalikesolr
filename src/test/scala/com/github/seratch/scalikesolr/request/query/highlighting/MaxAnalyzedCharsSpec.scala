package com.github.seratch.scalikesolr.request.query.highlighting

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MaxAnalyzedCharsSpec extends FlatSpec with ShouldMatchers {

  behavior of "MaxAnalyzedChars"

  it should "be available" in {
    val maxAnalyzedChars: Int = 0
    val instance = new MaxAnalyzedChars(maxAnalyzedChars)
    instance should not be null
    instance.getKey() should equal("hl.maxAnalyzedChars")
  }

}
