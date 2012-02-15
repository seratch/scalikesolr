package com.github.seratch.scalikesolr.request.query.group

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import com.github.seratch.scalikesolr.request.query._

@RunWith(classOf[JUnitRunner])
class GroupParamsSpec extends FlatSpec with ShouldMatchers {

  behavior of "GroupParams"

  it should "be available" in {
    val enabled: Boolean = false
    val field: GroupField = null
    val query: GroupQuery = null
    val rows: MaximumRowsReturned = null
    val start: StartRow = null
    val limit: GroupLimit = null
    val offset: GroupOffset = null
    val sort: Sort = null
    val groupSort: GroupSort = null
    val format: GroupFormat = null
    val main: AsMainResultWhenUsingSimpleFormat = null
    val ngroups: WithNumberOfGroups = null
    val cachePercent: GroupingCachePercent = null
    val instance = new GroupParams(enabled, field, query, rows, start, limit, offset, sort, groupSort, format, main, ngroups, cachePercent)
    instance should not be null
  }

}
