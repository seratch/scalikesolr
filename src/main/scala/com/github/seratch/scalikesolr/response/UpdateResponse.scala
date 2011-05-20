package com.github.seratch.scalikesolr.response

import common.ResponseHeader
import parser.ResponseParser
import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.WriterType
case class UpdateResponse(@BeanProperty val rawBody: String = "") {

  @BeanProperty lazy val responseHeader: ResponseHeader = ResponseParser.getResponseHeader(WriterType.Standard, rawBody)

}

