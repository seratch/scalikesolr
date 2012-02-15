package com.github.seratch.scalikesolr.request.query.morelikethis

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MaximumNumberOfQueryTermsSpec extends FlatSpec with ShouldMatchers {

  behavior of "MaximumNumberOfQueryTerms"

  it should "be available" in {
    val maxqt: Int = 0
    val instance = new MaximumNumberOfQueryTerms(maxqt)
    instance should not be null
  }

}
