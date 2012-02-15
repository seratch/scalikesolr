package com.github.seratch.scalikesolr.request.query.highlighting

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FragListBuilderSpec extends FlatSpec with ShouldMatchers {

  behavior of "FragListBuilder"

  it should "be available" in {
    val fragListBuilder: String = ""
    val instance = new FragListBuilder(fragListBuilder)
    instance should not be null
  }

}
