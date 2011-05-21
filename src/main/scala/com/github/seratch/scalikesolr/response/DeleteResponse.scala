package com.github.seratch.scalikesolr.response

import common.ResponseHeader
import parser.ResponseParser
import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.WriterType

case class DeleteResponse(@BeanProperty val writerType: WriterType = WriterType.Standard,
                          @BeanProperty val rawBody: String = "") {

  @BeanProperty lazy val responseHeader: ResponseHeader = ResponseParser.getResponseHeader(writerType, rawBody)

}

