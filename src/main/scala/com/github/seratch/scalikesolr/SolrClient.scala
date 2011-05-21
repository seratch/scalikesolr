package com.github.seratch.scalikesolr

import http.HttpClient
import java.net.URL
import reflect.BeanProperty
import io.Source
import org.slf4j.LoggerFactory
import java.lang.StringBuilder

trait SolrClient {

  def doQuery(request: QueryRequest): QueryResponse

  def doDIHCommand(request: DIHCommandRequest): DIHCommandResponse

  def doAddDocuments(request: AddRequest): AddResponse

  def doDeleteDocuments(request: DeleteRequest): DeleteResponse

  def doCommit(request: UpdateRequest): UpdateResponse

  def doOptimize(request: UpdateRequest): UpdateResponse

  def doRollback(request: UpdateRequest): UpdateResponse

  def doPing(request: PingRequest): PingResponse

}

class HttpSolrClient(@BeanProperty val url: URL) extends SolrClient {

  private val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.SolrClient")

  override def doQuery(request: QueryRequest): QueryResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val queryString = request.queryString
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core + "/select" + queryString
    log.debug("doQuery - Request URL : " + requestUrl)
    val responseBody = Source.fromURL(requestUrl, "UTF-8").mkString
    log.debug("doQuery - Response Body : " + responseBody)
    new QueryResponse(
      writerType = request.writerType,
      rawBody = responseBody
    )
  }

  override def doDIHCommand(request: DIHCommandRequest): DIHCommandResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val queryString = request.toQueryString
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core + "/dataimport" + queryString
    log.debug("doDIHCommand - Request URL : " + requestUrl)
    val responseBody = Source.fromURL(requestUrl, "UTF-8").mkString
    log.debug("doDIHCommand - Response Body : " + responseBody)
    new DIHCommandResponse(rawBody = responseBody)
  }

  override def doAddDocuments(request: AddRequest): AddResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core + "/update" + request.queryString
    log.debug("doAddDocuments - Request URL : " + requestUrl)
    val body = new StringBuilder
    body.append("<add>")
    request.documents foreach {
      case doc =>
        body.append("<doc>")
        doc.keys map {
          case key => {
            body.append("<field name=")
            body.append(""""""")
            body.append(key.toString)
            body.append(""""""")
            body.append(">")
            body.append(doc.get(key).toString)
            body.append("</field>")
          }
        }
        body.append("</doc>")
    }
    body.append("</add>")
    log.debug("doAddDocuments - Request Body : " + body.toString)
    val responseBody = HttpClient.post(requestUrl, body.toString, "text/xml", "UTF-8").content
    log.debug("doAddDocuments - Response Body : " + responseBody)
    new AddResponse(rawBody = responseBody)
  }

  override def doDeleteDocuments(request: DeleteRequest): DeleteResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core + "/update" + request.queryString
    log.debug("doAddDocuments - Request URL : " + requestUrl)
    val body = new StringBuilder
    body.append("<delete>")
    request.uniqueKeysToDelete foreach {
      case uniqueKey => {
        body.append("<id>")
        body.append(uniqueKey)
        body.append("</id>")
      }
    }
    request.queries foreach {
      case query => {
        body.append("<query>")
        body.append(query.toString)
        body.append("</query>")
      }
    }
    body.append("</delete>")
    log.debug("doDeleteDocuments - Request Body : " + body.toString)
    val responseBody = HttpClient.post(requestUrl, body.toString, "text/xml", "UTF-8").content
    log.debug("doDeleteDocuments - Response Body : " + responseBody)
    new DeleteResponse(rawBody = responseBody)
  }

  override def doCommit(request: UpdateRequest): UpdateResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core + "/update" + request.queryString
    log.debug("doCommit - Request URL : " + requestUrl)
    log.debug("doCommit - Request Body : <commit/>")
    val responseBody = HttpClient.post(requestUrl, "<commit/>", "text/xml", "UTF-8").content
    log.debug("doCommit - Response Body : " + responseBody)
    new UpdateResponse(rawBody = responseBody)
  }

  override def doOptimize(request: UpdateRequest): UpdateResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core + "/update" + request.queryString
    log.debug("doOptimize - Request URL : " + requestUrl)
    log.debug("doOptimize - Request Body : <optimize/>")
    val responseBody = HttpClient.post(requestUrl, "<optimize/>", "text/xml", "UTF-8").content
    log.debug("doOptimize - Response Body : " + responseBody)
    new UpdateResponse(rawBody = responseBody)
  }

  override def doRollback(request: UpdateRequest): UpdateResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core + "/update" + request.queryString
    log.debug("doRollback - Request URL : " + requestUrl)
    log.debug("doRollback - Request Body : <rollback/>")
    val responseBody = HttpClient.post(requestUrl, "<rollback/>", "text/xml", "UTF-8").content
    log.debug("doRollback - Response Body : " + responseBody)
    new UpdateResponse(rawBody = responseBody)
  }

  override def doPing(request: PingRequest): PingResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val queryString = request.queryString
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core + "/admin/ping" + queryString
    log.debug("doPing - Request URL : " + requestUrl)
    val responseBody = Source.fromURL(requestUrl, "UTF-8").mkString
    log.debug("doPing - Response Body : " + responseBody)
    new PingResponse(
      writerType = request.writerType,
      rawBody = responseBody
    )
  }

}