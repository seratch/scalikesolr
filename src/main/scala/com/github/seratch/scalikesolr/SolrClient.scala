/*
 * Copyright 2011 Kazuhiro Sera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.github.seratch.scalikesolr

import http.HttpClient
import java.net.URL
import reflect.BeanProperty
import io.Source
import org.slf4j.LoggerFactory
import util.{Log, XMLStringBuilder}

trait SolrClient {

  def doQuery(request: QueryRequest): QueryResponse

  def doDIHCommand(request: DIHCommandRequest): DIHCommandResponse

  def doAddDocuments(request: AddRequest): AddResponse

  def doDeleteDocuments(request: DeleteRequest): DeleteResponse

  def doCommit(request: UpdateRequest): UpdateResponse

  def doOptimize(request: UpdateRequest): UpdateResponse

  def doRollback(request: UpdateRequest): UpdateResponse

  def doPing(request: PingRequest): PingResponse

  def doAddDocumentsInCSV(request: UpdateRequest): UpdateResponse

  def doUpdateInXML(request: UpdateRequest): UpdateResponse

  def doUpdateInJSON(request: UpdateRequest): UpdateResponse

}

class HttpSolrClient(@BeanProperty val url: URL) extends SolrClient {

  private val log = new Log(LoggerFactory.getLogger(classOf[HttpSolrClient].getCanonicalName))

  private val LOG_PREFIX = "["
  private val LOG_PREFIX_URL = "URL: "
  private val LOG_PREFIX_REQ = "Request Body: "
  private val LOG_PREFIX_RESP = "Response Body: "
  private val COMMA = " , "
  private val LOG_SUFFIX = "]"

  private def logGet(requestUrl: => String): Unit = {
    log.debug(LOG_PREFIX + LOG_PREFIX_URL + requestUrl + LOG_SUFFIX)
  }

  private def logPost(requestUrl: => String, requestBody: => String): Unit = {
    log.debug(LOG_PREFIX + LOG_PREFIX_URL + requestUrl + COMMA + LOG_PREFIX_REQ + requestBody + LOG_SUFFIX)
  }

  private def logResponse(responseBody: => String): Unit = {
    log.debug(LOG_PREFIX + LOG_PREFIX_RESP + responseBody + LOG_SUFFIX)
  }

  override def doQuery(request: QueryRequest): QueryResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val queryString = request.queryString
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core + "/select" + queryString
    logGet(requestUrl)
    val responseBody = HttpClient.get(requestUrl, "UTF-8").content
    logResponse(responseBody)
    new QueryResponse(
      writerType = request.writerType,
      rawBody = responseBody
    )
  }

  override def doDIHCommand(request: DIHCommandRequest): DIHCommandResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val queryString = request.toQueryString
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core + "/dataimport" + queryString
    logGet(requestUrl)
    val responseBody = Source.fromURL(requestUrl, "UTF-8").mkString
    logResponse(responseBody)
    new DIHCommandResponse(
      writerType = request.writerType,
      rawBody = responseBody
    )
  }

  override def doAddDocuments(request: AddRequest): AddResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core + "/update" + request.toQueryString
    val xml = new XMLStringBuilder
    xml.append("<add>")
    request.documents foreach {
      case doc =>
        xml.append("<doc>")
        doc.keys map {
          case key => {
            xml.append("<field name=\"")
            xml.appendEscaped(key.toString)
            xml.append("\">")
            xml.appendEscaped(doc.get(key).toString)
            xml.append("</field>")
          }
        }
        xml.append("</doc>")
    }
    xml.append("</add>")
    val requestBody = xml.toString
    logPost(requestUrl, requestBody)
    val responseBody = HttpClient.post(requestUrl, requestBody, "text/xml", "UTF-8").content
    logResponse(responseBody)
    new AddResponse(
      writerType = request.writerType,
      rawBody = responseBody
    )
  }

  override def doDeleteDocuments(request: DeleteRequest): DeleteResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core +
      "/update" + request.toQueryString()
    val xml = new XMLStringBuilder
    xml.append("<delete>")
    request.uniqueKeysToDelete foreach {
      case uniqueKey => {
        xml.append("<id>")
        xml.appendEscaped(uniqueKey)
        xml.append("</id>")
      }
    }
    request.queries foreach {
      case query => {
        xml.append("<query>")
        xml.appendEscaped(query.toString)
        xml.append("</query>")
      }
    }
    xml.append("</delete>")
    val requestBody = xml.toString
    logPost(requestUrl, requestBody)
    val responseBody = HttpClient.post(requestUrl, xml.toString, "text/xml", "UTF-8").content
    logResponse(responseBody)
    new DeleteResponse(
      writerType = request.writerType,
      rawBody = responseBody
    )
  }

  override def doCommit(request: UpdateRequest): UpdateResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core +
      "/update" + request.toQueryString()
    val requestBody = "<commit/>"
    logPost(requestUrl, requestBody)
    val responseBody = HttpClient.post(requestUrl, requestBody, "text/xml", "UTF-8").content
    logResponse(responseBody)
    new UpdateResponse(
      writerType = request.writerType,
      rawBody = responseBody
    )
  }

  override def doOptimize(request: UpdateRequest): UpdateResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core +
      "/update" + request.toQueryString()
    val requestBody = "<optimize/>"
    logPost(requestUrl, requestBody)
    val responseBody = HttpClient.post(requestUrl, "<optimize/>", "text/xml", "UTF-8").content
    logResponse(responseBody)
    new UpdateResponse(
      writerType = request.writerType,
      rawBody = responseBody
    )
  }

  override def doRollback(request: UpdateRequest): UpdateResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core +
      "/update" + request.toQueryString()
    val requestBody = "<rollback/>"
    logPost(requestUrl, requestBody)
    val responseBody = HttpClient.post(requestUrl, "<rollback/>", "text/xml", "UTF-8").content
    logResponse(responseBody)
    new UpdateResponse(
      writerType = request.writerType,
      rawBody = responseBody
    )
  }

  override def doPing(request: PingRequest): PingResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val queryString = request.queryString
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core +
      "/admin/ping" + queryString
    logGet(requestUrl)
    val responseBody = HttpClient.get(requestUrl, "UTF-8").content
    logResponse(responseBody)
    new PingResponse(
      writerType = request.writerType,
      rawBody = responseBody
    )
  }

  override def doAddDocumentsInCSV(request: UpdateRequest): UpdateResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core +
      "/update/csv" + request.toQueryString()
    logPost(requestUrl, request.requestBody)
    val responseBody = HttpClient.post(requestUrl, request.requestBody, "text/xml", "UTF-8").content
    logResponse(responseBody)
    new UpdateResponse(
      writerType = request.writerType,
      rawBody = responseBody
    )
  }

  override def doUpdateInXML(request: UpdateRequest): UpdateResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core +
      "/update" + request.toQueryString()
    logPost(requestUrl, request.requestBody)
    val responseBody = HttpClient.post(requestUrl, request.requestBody, "text/xml", "UTF-8").content
    logResponse(responseBody)
    new UpdateResponse(
      writerType = request.writerType,
      rawBody = responseBody
    )
  }

  override def doUpdateInJSON(request: UpdateRequest): UpdateResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core +
      "/update/json" + request.toQueryString()
    logPost(requestUrl, request.requestBody)
    val responseBody = HttpClient.post(requestUrl, request.requestBody, "text/xml", "UTF-8").content
    logResponse(responseBody)
    new UpdateResponse(
      writerType = request.writerType,
      rawBody = responseBody
    )
  }

}
