package com.github.seratch.scalikesolr.request

import common.WriterType
import reflect.BeanProperty

import com.github.seratch.scalikesolr.{SolrDocument, SolrCore}
import util.QueryStringUtil

case class UpdateRequest(@BeanProperty var core: SolrCore = SolrCore(),
                         @BeanProperty var requestBody: String = "",
                         @BeanProperty var writerType: WriterType = WriterType.Standard,
                         @BeanProperty var additionalQueryString: String = "") {

  def this() = {
    this (SolrCore(), "", WriterType.Standard, "")
  }

  def toQueryString(): String = {
    val buf = new StringBuilder
    QueryStringUtil.appendIfExists(buf, writerType)
    "?" + buf.toString + additionalQueryString
  }

}