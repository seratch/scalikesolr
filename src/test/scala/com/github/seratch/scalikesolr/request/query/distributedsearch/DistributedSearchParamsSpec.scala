package com.github.seratch.scalikesolr.request.query.distributedsearch

import org.slf4j.LoggerFactory
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec

@RunWith(classOf[JUnitRunner])
class DistributedSearchParamsSpec extends FlatSpec with ShouldMatchers {

  behavior of "DistributedSearchParams"

  val log = LoggerFactory.getLogger(classOf[DistributedSearchParamsSpec])

  "toString" should "be available" in {
    val params = new DistributedSearchParams(List("server1:8080", "server2:8080"))
    params.toString should equal("shards=server1:8080,server2:8080")
  }

}