package com.github.seratch.scalikesolr.request.query.morelikethis

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MinimumTermFrequencySpec extends FlatSpec with ShouldMatchers {

  behavior of "MinimumTermFrequency"

  it should "be available" in {
    val mintf: Int = 0
    val instance = new MinimumTermFrequency(mintf)
    instance should not be null
  }

}
