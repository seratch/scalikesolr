package com.github.seratch.scalikesolr.request.util

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class QueryStringUtilSpec extends FlatSpec with ShouldMatchers {

  type ? = this.type // for IntelliJ IDEA

  "QueryStringUtil" should "be available" in {
    QueryStringUtil.isInstanceOf[Singleton] should equal(true)
  }

}
