package com.github.seratch.scalikesolr.request.query.highlighting

import org.scalatest._
import org.scalatest.matchers._

class FragSizeSpec extends FlatSpec with ShouldMatchers {

  behavior of "FragSize"

  it should "be available" in {
    val fragsize: Int = 0
    val instance = new FragSize(fragsize)
    instance should not be null
    instance.getKey() should equal("hl.fragsize")
  }

}
