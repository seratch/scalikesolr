package com.github.seratch.scalikesolr.response

import common.ResponseHeader
import parser.ResponseParser
import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.WriterType
import xml.XML
import util.parsing.json.JSON
import com.github.seratch.scalikesolr.util.JSONUtil

case class PingResponse(@BeanProperty val writerType: WriterType = WriterType.Standard,
                        @BeanProperty val rawBody: String = "") {

  private lazy val jsonMapFromRawBody: Map[String, Option[Any]] = {
    writerType match {
      case WriterType.JSON => JSONUtil.toMap(JSON.parseFull(rawBody))
      case _ => Map()
    }
  }

  @BeanProperty lazy val responseHeader: ResponseHeader = ResponseParser.getResponseHeader(writerType, rawBody)

  @BeanProperty lazy val status: String = {
    writerType match {
      case WriterType.Standard => {
        val xml = XML.loadString(rawBody)
        (xml \ "str").filter(item => (item \ "@name").text == "status")(0).text
      }
      case WriterType.JSON => {
        jsonMapFromRawBody.get("status").getOrElse("").toString
      }
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

}

