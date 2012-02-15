package com.github.seratch.scalikesolr.request.query.group

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GroupQuerySpec extends FlatSpec with ShouldMatchers {

  behavior of "GroupQuery"

  it should "be available" in {
    val query: String = ""
    val instance = new GroupQuery(query)
    instance should not be null
    instance.getKey() should equal("group.query")
  }

}
