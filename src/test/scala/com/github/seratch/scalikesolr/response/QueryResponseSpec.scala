package com.github.seratch.scalikesolr.response

import org.scalatest._
import org.scalatest.matchers._
import com.github.seratch.scalikesolr.request.common._
import org.apache.solr.common.util.NamedList

class QueryResponseSpec extends FlatSpec with ShouldMatchers {

  behavior of "QueryResponse"

  it should "be available" in {
    val writerType: WriterType = WriterType.Standard
    val rawBody: String = """
<?xml version="1.0" encoding="UTF-8"?>
<response>
<lst name="responseHeader">
  <int name="status">0</int>
  <int name="QTime">2</int>
  <lst name="params">
    <str name="indent">on</str>
    <str name="start">0</str>
    <str name="q">solr</str>
    <str name="rows">10</str>
    <str name="version">2.2</str>
  </lst>
</lst>
<result name="response" numFound="0" start="0"/>
</response>
                          """
    val rawJavabin: NamedList[Any] = new NamedList[Any]()
    val instance = new QueryResponse(writerType, rawBody, rawJavabin)
    instance should not be null
  }

}
