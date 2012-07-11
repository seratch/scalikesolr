package com.github.seratch.scalikesolr.http

import org.scalatest._
import org.scalatest.matchers._

class HttpResponseSpec extends FlatSpec with ShouldMatchers {

  behavior of "HttpResponse"

  it should "be available" in {
    val statusCode: Int = 200
    val headers: Map[String, List[String]] = Map("foo" -> List("bar"))
    val content: String = "xxxx"
    val response = new HttpResponse(statusCode, headers, content)
    response should not be null
    response.statusCode should equal(200)
    response.headers.isEmpty should equal(false)
    response.content should equal("xxxx")
  }

}
