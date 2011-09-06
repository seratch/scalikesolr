package com.github.seratch.scalikesolr.request.query.distributedsearch

import org.scalatest.Assertions
import org.slf4j.LoggerFactory
import com.github.seratch.scalikesolr.Solr
import java.net.URL
import org.junit.Test
import org.scalatest.matchers.ShouldMatchers

class DistributedSearchParamsTest extends Assertions with ShouldMatchers {

  val log = LoggerFactory.getLogger(classOf[DistributedSearchParamsTest])

  @Test
  def toStringIsAvailable() {
    val params = new DistributedSearchParams(List("server1:8080", "server2:8080"))
    params.toString should equal("shards=server1:8080,server2:8080")
  }
}