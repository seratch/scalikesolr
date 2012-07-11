package com.github.seratch.scalikesolr.request.query

import org.scalatest._
import org.scalatest.matchers._

class FilterQuerySpec extends FlatSpec with ShouldMatchers {

  behavior of "FilterQuery"

  it should "be available with single value" in {
    val fq = new FilterQuery(fq = "+popularity:[10 TO *] +section:0")
    fq.getKey() should equal("fq")
    fq.isMultiple() should equal(false)
    fq.getValue() should equal("+popularity:[10 TO *] +section:0")
  }

  it should "be available with multiple values" in {
    val fq = new FilterQuery(multiple = Seq(
      "popularity:[10 TO *]",
      "section:0"
    ))
    fq.getKey() should equal("fq")
    fq.isMultiple() should equal(true)
    fq.getValues() should equal(Seq(
      "popularity:[10 TO *]",
      "section:0"
    ))
  }

}
