package com.github.seratch.scalikesolr.request.query

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FilterQuerySpec extends FlatSpec with ShouldMatchers {

  behavior of "FilterQuery"

  it should "be available" in {
    val fq: String = ""
    val instance = new FilterQuery(fq)
    instance should not be null
    instance.getKey() should equal("fq")
  }

}
