package com.github.seratch.scalikesolr.request.query

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class VersionSpec extends FlatSpec with ShouldMatchers {

  behavior of "Version"

  it should "be available" in {
    val version: String = ""
    val instance = new Version(version)
    instance should not be null
  }

}
