package com.github.seratch.scalikesolr.response.query

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import com.github.seratch.scalikesolr.SolrDocument

@RunWith(classOf[JUnitRunner])
class MoreLikeThisSpec extends FlatSpec with ShouldMatchers {

  behavior of "MoreLikeThis"

  it should "be available" in {
    val numFound: Int = 0
    val start: Int = 0
    val idAndRecommendations: Map[String, List[SolrDocument]] = Map()
    val instance = new MoreLikeThis(numFound, start, idAndRecommendations)
    instance should not be null
  }

}
