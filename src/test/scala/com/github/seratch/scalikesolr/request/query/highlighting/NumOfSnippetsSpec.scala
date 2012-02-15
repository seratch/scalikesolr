package com.github.seratch.scalikesolr.request.query.highlighting

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class NumOfSnippetsSpec extends FlatSpec with ShouldMatchers {

  behavior of "NumOfSnippets"

  it should "be available" in {
    val snippets: Int = 0
    val instance = new NumOfSnippets(snippets)
    instance should not be null
  }

}
