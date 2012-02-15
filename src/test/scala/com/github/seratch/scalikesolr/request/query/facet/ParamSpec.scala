package com.github.seratch.scalikesolr.request.query.facet

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ParamSpec extends FlatSpec with ShouldMatchers {

  behavior of "Param"

  it should "be available" in {
    val param: String = ""
    val instance = new Param(param)
    instance should not be null
  }

}
