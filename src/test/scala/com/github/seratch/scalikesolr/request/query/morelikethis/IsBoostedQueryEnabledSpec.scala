package com.github.seratch.scalikesolr.request.query.morelikethis

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class IsBoostedQueryEnabledSpec extends FlatSpec with ShouldMatchers {

  behavior of "IsBoostedQueryEnabled"

  it should "be available" in {
    val boost: Boolean = false
    val instance = new IsBoostedQueryEnabled(boost)
    instance should not be null
  }

}
