package com.github.seratch.scalikesolr.request.query.group

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GroupOffsetSpec extends FlatSpec with ShouldMatchers {

  behavior of "GroupOffset"

  it should "be available" in {
    val offset: Int = 0
    val instance = new GroupOffset(offset)
    instance should not be null
  }

}
