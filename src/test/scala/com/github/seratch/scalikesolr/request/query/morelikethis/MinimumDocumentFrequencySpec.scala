package com.github.seratch.scalikesolr.request.query.morelikethis

import org.scalatest._
import org.scalatest.matchers._

class MinimumDocumentFrequencySpec extends FlatSpec with ShouldMatchers {

  behavior of "MinimumDocumentFrequency"

  it should "be available" in {
    val mindf: Int = 0
    val instance = new MinimumDocumentFrequency(mindf)
    instance should not be null
    instance.getKey() should equal("mlt.mindf")
  }

}
