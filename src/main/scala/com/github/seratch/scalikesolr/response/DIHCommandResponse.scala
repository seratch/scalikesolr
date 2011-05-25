package com.github.seratch.scalikesolr.response

import dih.{StatusMessages, InitArgs}
import java.lang.UnsupportedOperationException

import parser.ResponseParser
import xml.{Node, XML}
import reflect.BeanProperty

import common.ResponseHeader
import com.github.seratch.scalikesolr.request.common.WriterType
import com.github.seratch.scalikesolr.{SolrDocumentValue, SolrDocument}
import util.parsing.json.JSON
import com.github.seratch.scalikesolr.util.JSONUtil

case class DIHCommandResponse(@BeanProperty val writerType: WriterType = WriterType.Standard,
                              @BeanProperty val rawBody: String = "") {

  private lazy val jsonMapFromRawBody: Map[String, Option[Any]] = {
    writerType match {
      case WriterType.JSON => JSONUtil.toMap(JSON.parseFull(rawBody))
      case _ => Map()
    }
  }

  @BeanProperty lazy val responseHeader: ResponseHeader = ResponseParser.getResponseHeader(writerType, rawBody)

  @BeanProperty lazy val initArgs: InitArgs = {
    writerType match {
      case WriterType.Standard => {
        val xml = XML.loadString(rawBody)
        val initArgs = (xml \ "lst").filter(lst => (lst \ "@name").text == "initArgs")(0)
        val docMap = (initArgs.child.filter {
          lst => (lst \ "@name").text == "defaults"
        }(0).child map {
          case elem: Node => ((elem \ "@name").text, SolrDocumentValue(elem.text))
        }).toMap
        new InitArgs(defaults = SolrDocument(map = docMap))
      }
      case WriterType.JSON => {
        val initArgs = JSONUtil.toMap(jsonMapFromRawBody.get("initArgs"))
        val docMap = (initArgs.keysIterator map {
          case key => (key, new SolrDocumentValue(initArgs.getOrElse(key, "").toString))
        }).toMap
        new InitArgs(defaults = new SolrDocument(writerType = writerType, map = docMap))
      }
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

  @BeanProperty lazy val command: String = {
    writerType match {
      case WriterType.Standard => {
        val xml = XML.loadString(rawBody)
        (xml \ "str").filter(lst => (lst \ "@name").text == "command")(0).text
      }
      case WriterType.JSON => {
        jsonMapFromRawBody.get("command").getOrElse("").toString
      }
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

  @BeanProperty lazy val status: String = {
    writerType match {
      case WriterType.Standard => {
        val xml = XML.loadString(rawBody)
        (xml \ "str").filter(lst => (lst \ "@name").text == "status")(0).text
      }
      case WriterType.JSON => {
        jsonMapFromRawBody.get("status").getOrElse("").toString
      }
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

  @BeanProperty lazy val importResponse: String = {
    writerType match {
      case WriterType.Standard => {
        val xml = XML.loadString(rawBody)
        (xml \ "str").filter(lst => (lst \ "@name").text == "importResponse")(0).text
      }
      case WriterType.JSON => {
        jsonMapFromRawBody.get("importResponse").getOrElse("").toString
      }
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

  @BeanProperty lazy val statusMessages: StatusMessages = {
    writerType match {
      case WriterType.Standard => {
        val xml = XML.loadString(rawBody)
        val statusMessages = (xml \ "lst").filter(lst => (lst \ "@name").text == "statusMessages")(0)
        val docMap = (statusMessages.child map {
          case elem: Node => ((elem \ "@name").text, SolrDocumentValue(elem.text))
        }).toMap
        new StatusMessages(defaults = new SolrDocument(map = docMap))
      }
      case WriterType.JSON => {
        val doc = JSONUtil.toMap(jsonMapFromRawBody.get("statusMessages"))
        val docMap = (doc.keysIterator map {
          case docKey => (docKey, new SolrDocumentValue(doc.getOrElse(docKey, "").toString))
        }).toMap
        new StatusMessages(defaults = new SolrDocument(map = docMap))
      }
      case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

}
