package com.github.seratch.scalikesolr.request.query.morelikethis

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MaximumWordLengthAboveToBeIgnoredSpec extends FlatSpec with ShouldMatchers {

  behavior of "MaximumWordLengthAboveToBeIgnored"

  it should "be available" in {
    val maxwl: Int = 0
    val instance = new MaximumWordLengthAboveToBeIgnored(maxwl)
    instance should not be null
    instance.getKey() should equal("mlt.maxwl")
  }

}
