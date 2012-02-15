package com.github.seratch.scalikesolr.request.query.group

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GroupFunctionalQuerySpec extends FlatSpec with ShouldMatchers {

  behavior of "GroupFunctionalQuery"

  it should "be available" in {
    val func: String = ""
    val instance = new GroupFunctionalQuery(func)
    instance should not be null
    instance.getKey() should equal("group.func")
  }

}
