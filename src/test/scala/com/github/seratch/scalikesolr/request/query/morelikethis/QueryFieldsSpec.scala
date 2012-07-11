package com.github.seratch.scalikesolr.request.query.morelikethis

import org.scalatest._
import org.scalatest.matchers._

class QueryFieldsSpec extends FlatSpec with ShouldMatchers {

  behavior of "QueryFields"

  it should "be available" in {
    val qf: String = ""
    val instance = new QueryFields(qf)
    instance should not be null
    instance.getKey() should equal("mlt.qf")
  }

}
