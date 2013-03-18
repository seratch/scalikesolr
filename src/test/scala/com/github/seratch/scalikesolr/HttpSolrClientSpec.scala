package com.github.seratch.scalikesolr

import org.scalatest._
import org.scalatest.matchers._
import java.net.URL
import org.slf4j.LoggerFactory
import util._

class HttpSolrClientSpec extends FlatSpec with ShouldMatchers {

  behavior of "HttpSolrClient"

  it should "be available" in {
    val url: URL = new URL("http://localhost:8983/solr")
    val connectTimeout: Int = 0
    val readTimeout: Int = 0
    val log: Log = new Log(LoggerFactory.getLogger(classOf[HttpSolrClient].getCanonicalName))
    val client: HttpSolrClient = new HttpSolrClient(url, connectTimeout, readTimeout, log)
    client should not be null
  }

  /*
  it should "execute #doCommit" in {
    val url: URL = new URL("http://localhost:8983/solr")
    val connectTimeout: Int = 0
    val readTimeout: Int = 0
    val log: Log = new Log(LoggerFactory.getLogger(classOf[HttpSolrClient].getCanonicalName))
    val client: HttpSolrClient = new HttpSolrClient(url, connectTimeout, readTimeout, log)
    try {
      val response = client.doCommit()
      response.getResponseHeader().getStatus() should equal(0)
    } catch { case e: Exception => log.info(e.getMessage) }

  }
*/

  it should "execute #doRollback" in {
    val url: URL = new URL("http://localhost:8983/solr")
    val connectTimeout: Int = 0
    val readTimeout: Int = 0
    val log: Log = new Log(LoggerFactory.getLogger(classOf[HttpSolrClient].getCanonicalName))
    val client: HttpSolrClient = new HttpSolrClient(url, connectTimeout, readTimeout, log)
    val response = client.doRollback()
    response.getResponseHeader().getStatus() should equal(0)
  }

  it should "execute #doOptimize" in {
    val url: URL = new URL("http://localhost:8983/solr")
    val connectTimeout: Int = 0
    val readTimeout: Int = 0
    val log: Log = new Log(LoggerFactory.getLogger(classOf[HttpSolrClient].getCanonicalName))
    val client: HttpSolrClient = new HttpSolrClient(url, connectTimeout, readTimeout, log)
    try {
      val response = client.doOptimize()
      response.getResponseHeader().getStatus() should equal(0)
    } catch { case e: Exception => log.info(e.getMessage) }

  }

}
