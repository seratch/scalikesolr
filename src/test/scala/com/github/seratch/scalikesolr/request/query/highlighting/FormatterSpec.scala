package com.github.seratch.scalikesolr.request.query.highlighting

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FormatterSpec extends FlatSpec with ShouldMatchers {

  behavior of "Formatter"

  it should "be available" in {
    val formatter: String = ""
    val instance = new Formatter(formatter)
    instance should not be null
  }

}
