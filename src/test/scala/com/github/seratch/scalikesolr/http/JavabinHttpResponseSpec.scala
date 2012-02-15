package com.github.seratch.scalikesolr.http

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.apache.solr.common.util.NamedList

@RunWith(classOf[JUnitRunner])
class JavabinHttpResponseSpec extends FlatSpec with ShouldMatchers {

  behavior of "JavabinHttpResponse"

  it should "be available" in {
    val statusCode: Int = 200
    val headers: Map[String, List[String]] = Map("foo" -> List("bar"))
    val rawJavaBin: NamedList[Any] = null
    val response = new JavabinHttpResponse(statusCode, headers, rawJavaBin)
    response should not be null
    response should not be null
    response.statusCode should equal(200)
    response.headers.isEmpty should equal(false)
  }

}
