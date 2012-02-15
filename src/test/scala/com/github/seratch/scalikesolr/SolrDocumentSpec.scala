package com.github.seratch.scalikesolr

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SolrDocumentSpec extends FlatSpec with ShouldMatchers {

  behavior of "SolrDocument"

  it should "be available" in {
    val writerType: WriterType = null
    val rawBody: String = ""
    val rawJavabin: SolrjSolrDocument = null
    val map: Map[String, SolrDocumentValue] = Map()
    val instance = new SolrDocument(writerType, rawBody, rawJavabin, map)
    instance should not be null
  }

  it should "have keys" in {
    val writerType: WriterType = WriterType.Standard
    val rawBody: String = """<?xml version="1.0" encoding="UTF-8"?>
<response>
<lst name="responseHeader">
  <int name="status">0</int>
  <int name="QTime">25</int>
  <lst name="params">
    <str name="indent">on</str>
    <str name="start">0</str>
    <str name="q">test</str>
    <str name="rows">10</str>
    <str name="version">2.2</str>
  </lst>
</lst>
<result name="response" numFound="1" start="0">
  <doc>
    <str name="id">690974</str>
    <str name="body">aaa</str>
  </doc>
</result>
</response>
  """
    val rawJavabin: SolrjSolrDocument = null
    val map: Map[String, SolrDocumentValue] = Map()
    val doc = new SolrDocument(writerType, rawBody, rawJavabin, map)
    doc.keys().size should equal(2)
  }

  it should "have keysInJava" in {
    val writerType: WriterType = WriterType.Standard
    val rawBody: String = """<?xml version="1.0" encoding="UTF-8"?>
<response>
<lst name="responseHeader">
  <int name="status">0</int>
  <int name="QTime">25</int>
  <lst name="params">
    <str name="indent">on</str>
    <str name="start">0</str>
    <str name="q">test</str>
    <str name="rows">10</str>
    <str name="version">2.2</str>
  </lst>
</lst>
<result name="response" numFound="1" start="0">
  <doc>
    <str name="id">690974</str>
    <str name="body">aaa</str>
  </doc>
</result>
</response>
  """
    val rawJavabin: SolrjSolrDocument = null
    val map: Map[String, SolrDocumentValue] = Map()
    val doc = new SolrDocument(writerType, rawBody, rawJavabin, map)
    doc.keysInJava().size() should equal(2)
  }

}
