package com.github.seratch.scalikesolr.request.query

import org.scalatest._
import org.scalatest.matchers._

class ExplainOtherSpec extends FlatSpec with ShouldMatchers {

  behavior of "ExplainOther"

  it should "be available" in {
    val explainOther: String = ""
    val instance = new ExplainOther(explainOther)
    instance.getKey() should equal("explainOther")
    instance should not be null
  }

}
