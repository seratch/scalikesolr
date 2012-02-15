package com.github.seratch.scalikesolr.request.query.highlighting

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class IsPhraseHighlighterEnabledSpec extends FlatSpec with ShouldMatchers {

  behavior of "IsPhraseHigighterEnabled"

  it should "be available" in {
    val usePhraseHigighter: Boolean = false
    val instance = new IsPhraseHighlighterEnabled(usePhraseHigighter)
    instance should not be null
    instance.getKey() should equal("hl.usePhraseHighlighter")
  }

}
