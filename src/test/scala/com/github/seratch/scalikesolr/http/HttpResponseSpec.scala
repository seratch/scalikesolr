package com.github.seratch.scalikesolr.http

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class HttpResponseSpec extends FlatSpec with ShouldMatchers {

  behavior of "HttpResponse"

  it should "be available" in {
    val statusCode: Int = 200
    val headers: Map[String, List[String]] = Map()
    val content: String = ""
    val response = new HttpResponse(statusCode, headers, content)
    response should not be null
  }

}
