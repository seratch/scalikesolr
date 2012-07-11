package com.github.seratch.scalikesolr.request.query.highlighting

import org.scalatest._
import org.scalatest.matchers._

class FragmenterSpec extends FlatSpec with ShouldMatchers {

  behavior of "Fragmenter"

  it should "be available" in {
    val fragmenter: String = ""
    val instance = new Fragmenter(fragmenter)
    instance should not be null
    instance.getKey() should equal("hl.fragmenter")
  }

}
