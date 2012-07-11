package com.github.seratch.scalikesolr.request.query.group

import org.scalatest._
import org.scalatest.matchers._

class WithNumberOfGroupsSpec extends FlatSpec with ShouldMatchers {

  behavior of "WithNumberOfGroups"

  it should "be available" in {
    val ngroups: Boolean = false
    val instance = new WithNumberOfGroups(ngroups)
    instance should not be null
    instance.getKey() should equal("group.ngroups")
  }

}
