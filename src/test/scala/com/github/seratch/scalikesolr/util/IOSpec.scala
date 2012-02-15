package com.github.seratch.scalikesolr.util

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class IOSpec extends FlatSpec with ShouldMatchers {

  behavior of "IO"

  it should "be available" in {
    val io = IO
    io should not be null
  }

}
