package com.github.seratch.scalikesolr.util

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JSONUtilSpec extends FlatSpec with ShouldMatchers {

  behavior of "JSONUtil"

  "JSONUtil" should "be available" in {
    val jsonutil = JSONUtil
    jsonutil should not be null
  }

}
