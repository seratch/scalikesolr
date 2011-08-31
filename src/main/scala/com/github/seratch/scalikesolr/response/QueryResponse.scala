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

import parser.ResponseParser
import query._
import reflect.BeanProperty

import util.parsing.json.JSON
import com.github.seratch.scalikesolr.util.JSONUtil._

import com.github.seratch.scalikesolr.request.common._
import com.github.seratch.scalikesolr.response.common._
import collection.immutable.Map
import org.apache.solr.common.util.NamedList

case class QueryResponse(@BeanProperty val writerType: WriterType = WriterType.Standard,
                         @BeanProperty val rawBody: String = "",
                         @BeanProperty val rawJavaBin: NamedList[Any] = null) {

  @BeanProperty lazy val responseHeader: ResponseHeader
  = ResponseParser.getResponseHeader(writerType, rawBody, rawJavaBin)

  private lazy val jsonMapFromRawBody: Map[String, Option[Any]] = {
    writerType match {
      case WriterType.JSON => toMap(JSON.parseFull(rawBody))
      case _ => Map()
    }
  }

  @BeanProperty lazy val response: Response = Response.extract(
    writerType,
    rawBody,
    jsonMapFromRawBody,
    rawJavaBin
  )

  @BeanProperty lazy val groups: Groups = Groups.extract(
    writerType,
    rawBody,
    jsonMapFromRawBody,
    rawJavaBin
  )

  @BeanProperty lazy val highlightings: Highlightings = Highlightings.extract(
    writerType,
    rawBody,
    jsonMapFromRawBody,
    rawJavaBin
  )

  @BeanProperty lazy val moreLikeThis: MoreLikeThis = MoreLikeThis.extract(
    writerType,
    rawBody,
    jsonMapFromRawBody,
    rawJavaBin
  )

  @BeanProperty lazy val facet: Facet = Facet.extract(
    writerType,
    rawBody,
    jsonMapFromRawBody,
    rawJavaBin
  )

}
