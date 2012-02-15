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
    val headers: Map[String, List[String]] = Map()
    val rawJavaBin: NamedList[Any] = null
    val jtres = new JavabinHttpResponse(statusCode, headers, rawJavaBin)
    jtres should not be null
  }

}
