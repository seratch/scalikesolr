package com.github.seratch.scalikesolr.response

import common.ResponseHeader
import parser.ResponseParser
import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.WriterType
import xml.XML
import util.parsing.json.JSON

case class PingResponse(@BeanProperty val writerType: WriterType = WriterType.Standard,
                        @BeanProperty val rawBody: String = "") {

  @BeanProperty lazy val responseHeader: ResponseHeader = ResponseParser.getResponseHeader(writerType, rawBody)

  @BeanProperty lazy val status: String = {
    writerType match {
      case WriterType.Standard => {
        val xml = XML.loadString(rawBody)
        (xml \ "str").filter(item => (item \ "@name").text == "status")(0).text
      }
      case WriterType.JSON => {
        val jsonMap: Map[String, Option[Any]] = JSON.parseFull(rawBody).getOrElse(Map()).asInstanceOf[Map[String, Option[Any]]]
        jsonMap.get("status").getOrElse("").toString
      }
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

}

