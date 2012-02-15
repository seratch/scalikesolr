package com.github.seratch.scalikesolr.request.query.group

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GroupSortSpec extends FlatSpec with ShouldMatchers {

  behavior of "GroupSort"

  it should "be available" in {
    val sort: String = ""
    val instance = new GroupSort(sort)
    instance should not be null
  }

}
