package com.github.seratch.scalikesolr.request

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import common.WriterType
import query.Query
import com.github.seratch.scalikesolr.SolrCore

@RunWith(classOf[JUnitRunner])
class DeleteRequestSpec extends FlatSpec with ShouldMatchers {

  behavior of "DeleteRequest"

  it should "be available" in {
    val core: SolrCore = null
    val uniqueKeysToDelete: List[String] = Nil
    val queries: List[Query] = Nil
    val writerType: WriterType = null
    val additionalQueryString: String = ""
    val instance = new DeleteRequest(core, uniqueKeysToDelete, queries, writerType, additionalQueryString)
    instance should not be null
  }

}
