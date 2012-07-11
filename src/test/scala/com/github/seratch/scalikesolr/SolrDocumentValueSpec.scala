package com.github.seratch.scalikesolr

import org.slf4j.LoggerFactory
import java.util.Calendar

import org.joda.time.{ DateTime, LocalDate, LocalTime }
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec

class SolrDocumentValueSpec extends FlatSpec with ShouldMatchers {

  behavior of "SolrDocumentValue"

  val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.SolrDocumentValueSpec")

  "toDateOrElse" should "be available" in {
    {
      val value = new SolrDocumentValue("2010-12-31T23:59:59.000Z")
      val expected = new DateTime("2010-12-31T23:59:59.000Z")
      value.toDateOrElse(null).getTime should equal(expected.toDate.getTime)
    }
    {
      val value = new SolrDocumentValue("2010-12-31T23:59:59Z")
      val expected = new DateTime("2010-12-31T23:59:59.000Z")
      value.toDateOrElse(null).getTime should equal(expected.toDate.getTime)
    }
    {
      val value = new SolrDocumentValue("xxx")
      value.toDateOrElse(null) should be(null)
    }
  }

  "toDateTimeOrElse" should "be available" in {
    {
      val value = new SolrDocumentValue("2010-12-31T23:59:59.100Z")
      val expected = new DateTime("2010-12-31T23:59:59.10Z")
      value.toDateTimeOrElse(null) should equal(expected)
    }
    {
      val value = new SolrDocumentValue("2010-12-31T23:59:59Z")
      val expected = new DateTime("2010-12-31T23:59:59.000Z")
      value.toDateTimeOrElse(null) should equal(expected)
    }
    {
      val value = new SolrDocumentValue("xxx")
      value.toDateTimeOrElse(null) should be(null)
    }
  }

  "toLocalTimeOrElse" should "be available" in {
    {
      val value = new SolrDocumentValue("2010-12-31T23:59:59.000Z")
      val dateTime = new DateTime("2010-12-31T23:59:59.000Z")
      value.toLocalTimeOrElse(null) should equal(new LocalTime(dateTime))
    }
    {
      val value = new SolrDocumentValue("xxx")
      value.toLocalTimeOrElse(null) should be(null)
    }
  }

  "toLocalDateOrElse" should "be available" in {
    {
      val value = new SolrDocumentValue("2010-12-31T23:59:59.000Z")
      val dateTime = new DateTime("2010-12-31T23:59:59.000Z")
      value.toLocalDateOrElse(null) should equal(new LocalDate(dateTime))
    }
    {
      val value = new SolrDocumentValue("xxx")
      value.toLocalDateOrElse(null) should be(null)
    }
  }

  "toCalendarOrElse" should "be available" in {
    {
      val value = new SolrDocumentValue("2010-12-31T23:59:59.000Z")
      val dateTime = new DateTime("2010-12-31T23:59:59.000Z")
      var expected = Calendar.getInstance()
      expected.setTime(dateTime.toDate)
      value.toCalendarOrElse(null).getTimeInMillis should equal(expected.getTimeInMillis)
    }
    {
      val value = new SolrDocumentValue("xxx")
      value.toCalendarOrElse(null) should be(null)
    }
  }

}