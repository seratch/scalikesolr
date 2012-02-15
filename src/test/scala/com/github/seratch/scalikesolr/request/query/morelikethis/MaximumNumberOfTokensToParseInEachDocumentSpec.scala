package com.github.seratch.scalikesolr.request.query.morelikethis

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MaximumNumberOfTokensToParseInEachDocumentSpec extends FlatSpec with ShouldMatchers {

  behavior of "MaximumNumberOfTokensToParseInEachDocument"

  it should "be available" in {
    val maxntp: Int = 0
    val instance = new MaximumNumberOfTokensToParseInEachDocument(maxntp)
    instance should not be null
  }

}
