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

  def doCommit(request: UpdateRequest): UpdateResponse

  def doRollback(request: UpdateRequest): UpdateResponse

  def doPing(request: PingRequest): PingResponse

}

class HttpSolrClient(@BeanProperty val url: URL) extends SolrClient {

  private val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.SolrClient")

  override def doQuery(request: QueryRequest): QueryResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val queryString = request.queryString
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core + "/select/" + queryString
    log.debug("doQuery - Request URL : " + requestUrl)
    new QueryResponse(
      writerType = request.writerType,
      rawBody = Source.fromURL(requestUrl, "UTF-8").mkString
    )
  }

  override def doDIHCommand(request: DIHCommandRequest): DIHCommandResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val queryString = request.toQueryString
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core + "/dataimport/" + queryString
    log.debug("doDIHCommand - Request URL : " + requestUrl)
    new DIHCommandResponse(
      rawBody = Source.fromURL(requestUrl, "UTF-8").mkString
    )
  }

  override def doAddDocuments(request: AddRequest): AddResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core + "/update/"
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
    log.debug("doAddDocuments - Body : " + body.toString)
    new AddResponse(
      rawBody = HttpClient.post(requestUrl, body.toString, "text/xml", "UTF-8").content
    )
  }

  override def doCommit(request: UpdateRequest): UpdateResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core + "/update/"
    log.debug("doCommit - Request URL : " + requestUrl)
    new UpdateResponse(
      rawBody = HttpClient.post(requestUrl, "<commit/>", "text/xml", "UTF-8").content
    )
  }

  override def doRollback(request: UpdateRequest): UpdateResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core + "/update/"
    log.debug("doRollback - Request URL : " + requestUrl)
    new UpdateResponse(
      rawBody = HttpClient.post(requestUrl, "<rollback/>", "text/xml", "UTF-8").content
    )
  }

  override def doPing(request: PingRequest): PingResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val queryString = request.queryString
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core + "/admin/ping/" + queryString
    log.debug("doPing - Request URL : " + requestUrl)
    new PingResponse(
      writerType = request.writerType,
      rawBody = Source.fromURL(requestUrl, "UTF-8").mkString
    )
  }

}