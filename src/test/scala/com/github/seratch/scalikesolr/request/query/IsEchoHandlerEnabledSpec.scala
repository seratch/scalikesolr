package com.github.seratch.scalikesolr.request.query

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class IsEchoHandlerEnabledSpec extends FlatSpec with ShouldMatchers {

  behavior of "IsDebugQueryEnabled"

  it should "be available" in {
    val echoHandler: Boolean = false
    val instance = new IsEchoHandlerEnabled(echoHandler)
    instance should not be null
  }

}
