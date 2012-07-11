package com.github.seratch.scalikesolr.request.query.group

import org.scalatest._
import org.scalatest.matchers._

class GroupFieldSpec extends FlatSpec with ShouldMatchers {

  behavior of "GroupField"

  it should "be available" in {
    val field: String = ""
    val instance = new GroupField(field)
    instance should not be null
    instance.getKey() should equal("group.field")
  }

}
