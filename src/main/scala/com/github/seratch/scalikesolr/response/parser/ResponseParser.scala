package com.github.seratch.scalikesolr.response.parser

import com.github.seratch.scalikesolr.request.common.WriterType
import util.parsing.json.JSON
import com.github.seratch.scalikesolr.util.JSONUtil
import com.github.seratch.scalikesolr.response.common.ResponseHeader
import com.github.seratch.scalikesolr.{SolrDocument, SolrDocumentValue}
import xml.XML

object ResponseParser {

  def getResponseHeader(writerType: WriterType, rawBody: String) = {
    writerType match {
      case WriterType.Standard => {
        val xml = XML.loadString(rawBody)
        val intItems = xml \ "lst" \ "int"
        val paramsCandidates = (xml \ "lst" \ "list").filter(item => (item \ "@name").text == "params")
        if (paramsCandidates.size > 0) {
          val params = paramsCandidates(0)
          new ResponseHeader(
            intItems.filter(item => (item \ "@name").text == "status")(0).text.toInt,
            intItems.filter(item => (item \ "@name").text == "QTime")(0).text.toInt,
            SolrDocument(writerType = writerType, rawBody = params.toString)
          )
        } else {
          new ResponseHeader(
            intItems.filter(item => (item \ "@name").text == "status")(0).text.toInt,
            intItems.filter(item => (item \ "@name").text == "QTime")(0).text.toInt
          )
        }
      }
      case WriterType.JSON => {
        val jsonMap = JSONUtil.toMap(JSON.parseFull(rawBody))
        val responseHeader = JSONUtil.toMap(jsonMap.get("responseHeader"))
        val status = JSONUtil.normalizeNum(responseHeader.get("status").getOrElse("0").toString).toInt
        val qTime = JSONUtil.normalizeNum(responseHeader.get("QTime").getOrElse("0").toString).toInt
        val params = JSONUtil.toMap(responseHeader.get("params"))
        val docMap = (params.keysIterator map {
          case key => (key, new SolrDocumentValue(params.getOrElse(key, "").toString))
        }).toMap
        new ResponseHeader(status, qTime, new SolrDocument(writerType = writerType, map = docMap))
      }
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

}