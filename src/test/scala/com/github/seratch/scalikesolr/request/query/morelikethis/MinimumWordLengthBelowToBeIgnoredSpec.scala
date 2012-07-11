package com.github.seratch.scalikesolr.request.query.morelikethis

import org.scalatest._
import org.scalatest.matchers._

class MinimumWordLengthBelowToBeIgnoredSpec extends FlatSpec with ShouldMatchers {

  behavior of "MinimumWordLengthBelowToBeIgnored"

  it should "be available" in {
    val minwl: Int = 0
    val instance = new MinimumWordLengthBelowToBeIgnored(minwl)
    instance should not be null
    instance.getKey() should equal("mlt.minwl")
  }

}
