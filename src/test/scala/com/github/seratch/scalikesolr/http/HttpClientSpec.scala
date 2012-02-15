package com.github.seratch.scalikesolr.http

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class HttpClientSpec extends FlatSpec with ShouldMatchers {

  behavior of "HttpClient"

  it should "be available" in {
    val client = new HttpClient
    val response = client.get("http://seratch.github.com/scalikesolr", "UTF-8")
    response.statusCode should equal(200)
  }

}
