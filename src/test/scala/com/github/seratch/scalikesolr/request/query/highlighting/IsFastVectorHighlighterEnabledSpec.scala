package com.github.seratch.scalikesolr.request.query.highlighting

import org.scalatest._
import org.scalatest.matchers._

class IsFastVectorHighlighterEnabledSpec extends FlatSpec with ShouldMatchers {

  behavior of "IsFastVectorHighlighterEnabled"

  it should "be available" in {
    val useFastVectorHighlighter: Boolean = false
    val instance = new IsFastVectorHighlighterEnabled(useFastVectorHighlighter)
    instance should not be null
    instance.getKey() should equal("hl.useFastVectorHighlighter")
  }

}
