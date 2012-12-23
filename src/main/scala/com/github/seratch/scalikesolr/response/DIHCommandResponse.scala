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

package com.github.seratch.scalikesolr.response

import dih.{ StatusMessages, InitArgs }
import java.lang.UnsupportedOperationException

import parser.ResponseParser
import scala.xml.{ Node, XML }
import scala.beans.BeanProperty

import common.ResponseHeader
import com.github.seratch.scalikesolr.request.common.WriterType
import com.github.seratch.scalikesolr.{ SolrDocumentValue, SolrDocument }

case class DIHCommandResponse(@BeanProperty val writerType: WriterType = WriterType.Standard,
    @BeanProperty val rawBody: String = "") {

  @BeanProperty
  lazy val responseHeader: ResponseHeader = ResponseParser.getResponseHeader(
    writerType,
    rawBody
  )

  @BeanProperty
  lazy val initArgs: InitArgs = {
    writerType match {
      case WriterType.Standard =>
        val xml = XML.loadString(rawBody)
        val initArgs = (xml \ "lst").filter(lst => (lst \ "@name").text == "initArgs")(0)
        val docMap = initArgs.child.filter {
          lst => (lst \ "@name").text == "defaults"
        }(0).child.map {
          case elem: Node => ((elem \ "@name").text, SolrDocumentValue(elem.text))
        }.toMap
        new InitArgs(defaults = SolrDocument(map = docMap))
      case other =>
        throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

  @BeanProperty
  lazy val command: String = {
    writerType match {
      case WriterType.Standard =>
        val xml = XML.loadString(rawBody)
        (xml \ "str").filter(lst => (lst \ "@name").text == "command")(0).text
      case other =>
        throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

  @BeanProperty
  lazy val status: String = {
    writerType match {
      case WriterType.Standard =>
        val xml = XML.loadString(rawBody)
        (xml \ "str").filter(lst => (lst \ "@name").text == "status")(0).text
      case other =>
        throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

  @BeanProperty
  lazy val importResponse: String = {
    writerType match {
      case WriterType.Standard =>
        val xml = XML.loadString(rawBody)
        (xml \ "str").filter(lst => (lst \ "@name").text == "importResponse")(0).text
      case other =>
        throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

  @BeanProperty
  lazy val statusMessages: StatusMessages = {
    writerType match {
      case WriterType.Standard =>
        val xml = XML.loadString(rawBody)
        val statusMessages = (xml \ "lst").filter(lst => (lst \ "@name").text == "statusMessages")(0)
        val docMap = statusMessages.child.map {
          case elem: Node => ((elem \ "@name").text, SolrDocumentValue(elem.text))
        }.toMap
        new StatusMessages(defaults = new SolrDocument(map = docMap))
      case other =>
        throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
    }
  }

}
