package com.github.seratch.scalikesolr.request.util

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import com.github.seratch.scalikesolr.request.common.RequestParam

@RunWith(classOf[JUnitRunner])
class QueryStringUtilSpec extends FlatSpec with ShouldMatchers {

  behavior of "QueryStringUtil"

  it should "be available" in {
    val singleton = QueryStringUtil
    singleton should not be null
  }

  it should "execute appendIfExists when the param exits" in {
    val buf = new StringBuilder
    val rp = new RequestParam {
      override def isEmpty() = false
      override def getKey() = "param"
      override def getValue() = "true"
    }
    QueryStringUtil.appendIfExists(buf, rp)
    buf.toString should equal("param=true")
  }

  it should "execute appendIfExists when the param is empty" in {
    val buf = new StringBuilder
    val rp = new RequestParam {
      override def isEmpty() = true
      override def getKey() = "param"
      override def getValue() = null
    }
    QueryStringUtil.appendIfExists(buf, rp)
    buf.toString should equal("")
  }

  it should "execute appendIfExists when the value is not empty" in {
    val buf = new StringBuilder
    val rp = new RequestParam {
      override def isEmpty() = false
      override def getKey() = "param"
      override def getValue() = ""
    }
    QueryStringUtil.appendIfExists(buf, rp)
    buf.toString should equal("param=")
  }

  it should "execute appendIfExists when the value is multiple" in {
    val buf = new StringBuilder
    val rp = new RequestParam {
      override def isEmpty() = false
      override def isMultiple() = true
      override def getKey() = "k"
      override def getValue() = ""
      override def getValues() = Seq("a", "b", "c")
    }
    QueryStringUtil.appendIfExists(buf, rp)
    buf.toString should equal("k=a&k=b&k=c")
  }

}
