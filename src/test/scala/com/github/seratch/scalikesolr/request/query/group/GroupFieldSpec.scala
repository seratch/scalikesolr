package com.github.seratch.scalikesolr.request.query.group

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GroupFieldSpec extends FlatSpec with ShouldMatchers {

  behavior of "GroupField"

  it should "be available" in {
    val field: String = ""
    val instance = new GroupField(field)
    instance should not be null
    instance.getKey() should equal("group.field")
  }

}
