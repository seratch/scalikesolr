package com.github.seratch.scalikesolr.util

import org.scalatest.Assertions
import org.junit.Test
import org.slf4j.LoggerFactory

class XMLStringBuilderTest extends Assertions {

  val log = LoggerFactory.getLogger("XMLStringBuilderTest")

  @Test def append() {
    val xml = new XMLStringBuilder
    xml.append("\"<req>abc&'")
    log.info("append: " + xml.toString)
    assert(xml.toString == "\"<req>abc&'")
  }

  @Test def appendEscaped() {
    val xml = new XMLStringBuilder
    xml.appendEscaped("\"<req>abc&'")
    log.info("appendEscaped: " + xml.toString)
    assert(xml.toString == "&quot;&lt;req&gt;abc&amp;&apos;")
  }

}