package com.github.seratch.scalikesolr.request.query.group

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class WithNumberOfGroupsSpec extends FlatSpec with ShouldMatchers {

  behavior of "WithNumberOfGroups"

  it should "be available" in {
    val ngroups: Boolean = false
    val instance = new WithNumberOfGroups(ngroups)
    instance should not be null
    instance.getKey() should equal("group.ngroups")
  }

}
