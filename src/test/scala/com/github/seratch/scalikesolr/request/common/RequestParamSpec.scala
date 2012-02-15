package com.github.seratch.scalikesolr.request.common

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class RequestParamSpec extends FlatSpec with ShouldMatchers {

  behavior of "RequestParam"

  it should "be available" in {
    val mixedin = new Object with RequestParam {
      override def isEmpty() = false
      override def getKey() = "mixedin"
      override def getValue() = "true"
    }
    mixedin should not be null
  }

}
