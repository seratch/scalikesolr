package com.github.seratch.scalikesolr.request.query.highlighting

import org.scalatest._
import org.scalatest.matchers._

class SimpleSuffixSpec extends FlatSpec with ShouldMatchers {

  behavior of "SimpleSuffix"

  it should "be available" in {
    val simplePost: String = ""
    val instance = new SimpleSuffix(simplePost)
    instance should not be null
    instance.getKey() should equal("hl.simple.post")
  }

}
