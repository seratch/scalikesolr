package com.github.seratch.scalikesolr.request.query.morelikethis

import org.scalatest._
import org.scalatest.matchers._

class FieldsToUseForSimilaritySpec extends FlatSpec with ShouldMatchers {

  behavior of "FieldsToUseForSimilarity"

  it should "be available" in {
    val fl: String = ""
    val instance = new FieldsToUseForSimilarity(fl)
    instance should not be null
    instance.getKey() should equal("mlt.fl")
  }

}
