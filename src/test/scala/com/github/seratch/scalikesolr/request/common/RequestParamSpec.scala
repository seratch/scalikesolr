package com.github.seratch.scalikesolr.request.common

import org.scalatest._
import org.scalatest.matchers._

class RequestParamSpec extends FlatSpec with ShouldMatchers {

  behavior of "RequestParam"

  it should "be available" in {
    val param = new Object with RequestParam {
      override def isEmpty() = false

      override def getKey() = "param"

      override def getValue() = "true"
    }
    param should not be null
    param.getKey() should equal("param")
    param.getValue() should equal("true")
  }

}
