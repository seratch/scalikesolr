package com.github.seratch.scalikesolr.request.common

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class WriterTypeSpec extends FlatSpec with ShouldMatchers {

  behavior of "WriterType"

  it should "be available" in {
    val wt: String = ""
    val instance = new WriterType(wt)
    instance should not be null
  }

}
