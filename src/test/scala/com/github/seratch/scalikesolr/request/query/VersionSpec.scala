package com.github.seratch.scalikesolr.request.query

import org.scalatest._
import org.scalatest.matchers._

class VersionSpec extends FlatSpec with ShouldMatchers {

  behavior of "Version"

  it should "be available" in {
    val version: String = ""
    val instance = new Version(version)
    instance should not be null
    instance.getKey() should equal("version")
  }

}
