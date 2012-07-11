package com.github.seratch.scalikesolr.request.query.group

import org.scalatest._
import org.scalatest.matchers._

class GroupLimitSpec extends FlatSpec with ShouldMatchers {

  behavior of "GroupLimit"

  it should "be available" in {
    val limit: Int = 0
    val instance = new GroupLimit(limit)
    instance should not be null
    instance.getKey() should equal("group.limit")
  }

}
