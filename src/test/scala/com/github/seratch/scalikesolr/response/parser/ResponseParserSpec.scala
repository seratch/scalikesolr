package com.github.seratch.scalikesolr.response.parser

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ResponseParserSpec extends FlatSpec with ShouldMatchers {

  behavior of "ResponseParser"

  it should "be available" in {
    ResponseParser.isInstanceOf[Singleton] should equal(true)
  }

}
