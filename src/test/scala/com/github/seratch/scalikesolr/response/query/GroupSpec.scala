package com.github.seratch.scalikesolr.response.query

import org.scalatest._
import org.scalatest.matchers._
import com.github.seratch.scalikesolr._
import com.github.seratch.scalikesolr.response.query.{ Group => SolrGroup }

class GroupSpec extends FlatSpec with ShouldMatchers {

  behavior of "Group"

  it should "be available" in {
    val numFound: Int = 0
    val start: Int = 0
    val groupValue: String = ""
    val documents: List[SolrDocument] = Nil
    val instance = new SolrGroup(numFound, start, groupValue, documents)
    instance should not be null
  }

  it should "have getDocumentsInJava" in {
    val numFound: Int = 0
    val start: Int = 0
    val groupValue: String = ""
    val documents: List[SolrDocument] = List(SolrDocument(), SolrDocument())
    val group: SolrGroup = new SolrGroup(numFound, start, groupValue, documents)
    group.getDocumentsInJava().size() should equal(2)
  }

}
