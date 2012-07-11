package com.github.seratch.scalikesolr.request.query

import org.scalatest._
import org.scalatest.matchers._

class QuerySpec extends FlatSpec with ShouldMatchers {

  behavior of "Query"

  it should "be available" in {
    val q: String = ""
    val instance = new Query(q)
    instance should not be null
    instance.getKey() should equal("q")
  }

}
