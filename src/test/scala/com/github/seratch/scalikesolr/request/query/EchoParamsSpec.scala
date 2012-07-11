package com.github.seratch.scalikesolr.request.query

import org.scalatest._
import org.scalatest.matchers._

class EchoParamsSpec extends FlatSpec with ShouldMatchers {

  behavior of "EchoParams"

  it should "be available" in {
    val echoParams: String = "foo"
    val instance = new EchoParams(echoParams)
    instance should not be null
    instance.getKey() should equal("echoParams")
    instance.getValue() should equal("foo")
  }

}
