package com.github.seratch.scalikesolr

import org.specs.Specification
import org.slf4j.LoggerFactory
import org.joda.time.format.DateTimeFormat
import java.util.{Calendar, Locale}

import org.junit.runner.RunWith
import org.joda.time.{DateTime, LocalDate, LocalTime}
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SolrDocumentValueSuite extends FunSuite with ShouldMatchers {

  type ? = this.type

  val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.SolrDocumentValueSpec")

  test("toDateOrElse") {
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

  test("toDateTimeOrElse") {
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

  test("toLocalTimeOrElse") {
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

  test("toLocalDateOrElse") {
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

  test("toCalendarOrElse") {
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