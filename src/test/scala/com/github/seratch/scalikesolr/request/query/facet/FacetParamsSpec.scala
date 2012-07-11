package com.github.seratch.scalikesolr.request.query.facet

import org.scalatest._
import org.scalatest.matchers._

class FacetParamsSpec extends FlatSpec with ShouldMatchers {

  behavior of "FacetParams"

  it should "be available" in {
    val enabled: Boolean = false
    val params: List[FacetParam] = Nil
    val instance = new FacetParams(enabled, params)
    instance should not be null
  }

}
