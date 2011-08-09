package com.github.seratch.scalikesolr

import org.specs.Specification
import org.slf4j.LoggerFactory
import org.joda.time.format.DateTimeFormat
import java.util.{Calendar, Locale}

import org.junit.runner.RunWith
import org.specs._
import org.specs.matcher._
import org.specs.runner.{JUnitSuiteRunner, JUnit}
import org.joda.time.{DateTime, LocalDate, LocalTime}

@RunWith(classOf[JUnitSuiteRunner])
class SolrDocumentValueSpec extends Specification with JUnit {

  val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.SolrDocumentValueSpec")

  "toDateOrElse" should {
    "be available" in {
      val value = new SolrDocumentValue("2010-12-31T23:59:59.000Z")
      val expected = new DateTime("2010-12-31T23:59:59.000Z")
      value.toDateOrElse(null).getTime must equalTo(expected.toDate.getTime)
    }
    "be available 2" in {
      val value = new SolrDocumentValue("2010-12-31T23:59:59Z")
      val expected = new DateTime("2010-12-31T23:59:59.000Z")
      value.toDateOrElse(null).getTime must equalTo(expected.toDate.getTime)
    }
    "work fine even if the arg is invalid value" in {
      val value = new SolrDocumentValue("xxx")
      value.toDateOrElse(null) must beNull
    }
  }

  "toDateTimeOrElse" should {
    "be available" in {
      val value = new SolrDocumentValue("2010-12-31T23:59:59.100Z")
      val expected = new DateTime("2010-12-31T23:59:59.10Z")
      value.toDateTimeOrElse(null) must equalTo(expected)
    }
    "be available 2" in {
      val value = new SolrDocumentValue("2010-12-31T23:59:59Z")
      val expected = new DateTime("2010-12-31T23:59:59.000Z")
      value.toDateTimeOrElse(null) must equalTo(expected)
    }
    "work fine even if the arg is invalid value" in {
      val value = new SolrDocumentValue("xxx")
      value.toDateTimeOrElse(null) must beNull
    }
  }

  "toLocalTimeOrElse" should {
    "be available" in {
      val value = new SolrDocumentValue("2010-12-31T23:59:59.000Z")
      val dateTime = new DateTime("2010-12-31T23:59:59.000Z")
      value.toLocalTimeOrElse(null) must equalTo(new LocalTime(dateTime))
    }
    "work fine even if the arg is invalid value" in {
      val value = new SolrDocumentValue("xxx")
      value.toLocalTimeOrElse(null) must beNull
    }
  }

  "toLocalDateOrElse" should {
    "be available" in {
      val value = new SolrDocumentValue("2010-12-31T23:59:59.000Z")
      val dateTime = new DateTime("2010-12-31T23:59:59.000Z")
      value.toLocalDateOrElse(null) must equalTo(new LocalDate(dateTime))
    }
    "work fine even if the arg is invalid value" in {
      val value = new SolrDocumentValue("xxx")
      value.toLocalDateOrElse(null) must beNull
    }
  }

  "toCalendarOrElse" should {
    "be available" in {
      val value = new SolrDocumentValue("2010-12-31T23:59:59.000Z")
      val dateTime = new DateTime("2010-12-31T23:59:59.000Z")
      var expected = Calendar.getInstance()
      expected.setTime(dateTime.toDate)
      value.toCalendarOrElse(null).getTimeInMillis must equalTo(expected.getTimeInMillis)
    }
    "work fine even if the arg is invalid value" in {
      val value = new SolrDocumentValue("xxx")
      value.toCalendarOrElse(null) must beNull
    }
  }

}