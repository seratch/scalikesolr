package com.github.seratch.scalikesolr.request.query.group

import org.scalatest._
import org.scalatest.matchers._

class GroupingCachePercentSpec extends FlatSpec with ShouldMatchers {

  behavior of "GroupingCachePercent"

  it should "be available" in {
    val cachePercent: Int = 0
    val instance = new GroupingCachePercent(cachePercent)
    instance should not be null
    instance.getKey() should equal("group.cache.percent")
  }

}
