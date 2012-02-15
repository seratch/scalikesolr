package com.github.seratch.scalikesolr.request.query.highlighting

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class IsMultiTermHighlightingEnabledSpec extends FlatSpec with ShouldMatchers {

  behavior of "IsMultiTermHighlightingEnabled"

  it should "be available" in {
    val higightMultiTerm: Boolean = false
    val instance = new IsMultiTermHighlightingEnabled(higightMultiTerm)
    instance should not be null
    instance.getKey() should equal("hl.highlightMultiTerm")
  }

}
