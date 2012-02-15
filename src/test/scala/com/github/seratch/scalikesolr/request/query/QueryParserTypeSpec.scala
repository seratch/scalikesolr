package com.github.seratch.scalikesolr.request.query

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class QueryParserTypeSpec extends FlatSpec with ShouldMatchers {

  behavior of "QueryParserType"

  it should "be available" in {
    val defType: String = ""
    val instance = new QueryParserType(defType)
    instance should not be null
    instance.getKey() should equal("defType")
  }

}
