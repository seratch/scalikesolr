package com.github.seratch.scalikesolr.response.query

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import com.github.seratch.scalikesolr.SolrDocument

@RunWith(classOf[JUnitRunner])
class HighlightingsSpec extends FlatSpec with ShouldMatchers {

  behavior of "Highlightings"

  it should "be available" in {
    val highlightings: Map[String, SolrDocument] = Map()
    val instance = new Highlightings(highlightings)
    instance should not be null
  }

  it should "have keysInJava" in {
    val highlightings: Map[String, SolrDocument] = Map()
    val hl = new Highlightings(highlightings)
    hl.keysInJava().size() should equal(0)
  }

}
