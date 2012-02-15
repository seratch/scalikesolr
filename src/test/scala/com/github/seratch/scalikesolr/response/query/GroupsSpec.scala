package com.github.seratch.scalikesolr.response.query

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import com.github.seratch.scalikesolr.response.query.{ Group => SolrGroup }

@RunWith(classOf[JUnitRunner])
class GroupsSpec extends FlatSpec with ShouldMatchers {

  behavior of "Groups"

  it should "be available" in {
    val matches: Int = 0
    val groups: List[SolrGroup] = Nil
    val instance = new Groups(matches, groups)
    instance should not be null
  }

  it should "have getDocumentsInJava" in {
    val matches: Int = 0
    val groups: List[SolrGroup] = List()
    val gs = new Groups(matches, groups)
    gs.getGroupsInJava().size() should equal(0)
  }

}
