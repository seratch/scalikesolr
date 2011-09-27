package com.github.seratch.scalikesolr.request.query.distributedsearch

import org.slf4j.LoggerFactory
import com.github.seratch.scalikesolr.Solr
import java.net.URL
import org.junit.Test
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite

@RunWith(classOf[JUnitRunner])
class DistributedSearchParamsSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  val log = LoggerFactory.getLogger(classOf[DistributedSearchParamsSuite])

  test("toStringIsAvailable") {
    val params = new DistributedSearchParams(List("server1:8080", "server2:8080"))
    params.toString should equal("shards=server1:8080,server2:8080")
  }

}