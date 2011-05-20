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

  def doAddDocuments(request: UpdateRequest): UpdateResponse

  def doCommit(request: UpdateRequest): UpdateResponse

  def doRollback(request: UpdateRequest): UpdateResponse

  def doAddDocumentsAndCommit(request: UpdateRequest): UpdateResponse = {
    doAddDocuments(request)
    doCommit(request)
  }

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

  override def doAddDocuments(request: UpdateRequest): UpdateResponse = {
    val core = if (request.core.name.isEmpty) "" else "/" + request.core.name
    val requestUrl = url.getProtocol + "://" + url.getHost + ":" + url.getPort + url.getPath + core + "/update/"
    log.debug("doAddDocuments - Request URL : " + requestUrl)
    val dataBinary = new StringBuilder
    dataBinary.append("<add>")
    request.documents foreach {
      case doc =>
        dataBinary.append("<doc>")
        doc.keys map {
          case key => {
            dataBinary.append("<field name=")
            dataBinary.append(""""""")
            dataBinary.append(key.toString)
            dataBinary.append(""""""")
            dataBinary.append(">")
            dataBinary.append(doc.get(key).toString)
            dataBinary.append("</field>")
          }
        }
        dataBinary.append("</doc>")
    }
    dataBinary.append("</add>")
    log.debug("doAddDocuments - DataBinary : " + dataBinary.toString)
    new UpdateResponse(
      rawBody = HttpClient.post(requestUrl, dataBinary.toString, "text/xml", "UTF-8").content
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