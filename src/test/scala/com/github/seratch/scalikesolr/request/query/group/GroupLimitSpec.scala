package com.github.seratch.scalikesolr.request.query.group

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GroupLimitSpec extends FlatSpec with ShouldMatchers {

  behavior of "GroupLimit"

  it should "be available" in {
    val limit: Int = 0
    val instance = new GroupLimit(limit)
    instance should not be null
  }

}
