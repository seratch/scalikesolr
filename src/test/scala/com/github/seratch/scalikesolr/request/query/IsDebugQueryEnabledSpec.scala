package com.github.seratch.scalikesolr.request.query

import org.scalatest._
import org.scalatest.matchers._

class IsDebugQueryEnabledSpec extends FlatSpec with ShouldMatchers {

  behavior of "IsDebugQueryEnabled"

  it should "be available" in {
    val debugQuery: Boolean = false
    val instance = new IsDebugQueryEnabled(debugQuery)
    instance should not be null
    instance.getKey() should equal("debugQuery")
  }

}
