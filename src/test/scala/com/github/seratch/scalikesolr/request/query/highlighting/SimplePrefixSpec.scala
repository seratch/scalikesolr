package com.github.seratch.scalikesolr.request.query.highlighting

import org.scalatest._
import org.scalatest.matchers._

class SimplePrefixSpec extends FlatSpec with ShouldMatchers {

  behavior of "SimplePrefix"

  it should "be available" in {
    val simplePre: String = ""
    val instance = new SimplePrefix(simplePre)
    instance should not be null
    instance.getKey() should equal("hl.simple.pre")
  }

}
