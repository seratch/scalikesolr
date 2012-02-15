package com.github.seratch.scalikesolr.response.query

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import com.github.seratch.scalikesolr.SolrDocument

@RunWith(classOf[JUnitRunner])
class ResponseSpec extends FlatSpec with ShouldMatchers {

  behavior of "Response"

  it should "be available" in {
    val numFound: Int = 0
    val start: Int = 0
    val documents: List[SolrDocument] = Nil
    val instance = new Response(numFound, start, documents)
    instance should not be null
  }

}
