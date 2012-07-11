package com.github.seratch.scalikesolr.request.query.highlighting

import org.scalatest._
import org.scalatest.matchers._

class FragListBuilderSpec extends FlatSpec with ShouldMatchers {

  behavior of "FragListBuilder"

  it should "be available" in {
    val fragListBuilder: String = ""
    val instance = new FragListBuilder(fragListBuilder)
    instance should not be null
    instance.getKey() should equal("hl.fragListBuilder")
  }

}
