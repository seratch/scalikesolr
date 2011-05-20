package com.github.seratch.scalikesolr.request

import common.WriterType
import reflect.BeanProperty

import com.github.seratch.scalikesolr.SolrCore
import util.QueryStringUtil

case class PingRequest(@BeanProperty var core: SolrCore = SolrCore(),
                       @BeanProperty var writerType: WriterType = WriterType()) {

  def this() = {
    this (SolrCore(), WriterType())
  }

  def queryString(): String = {
    val buf = new StringBuilder
    QueryStringUtil.appendIfExists(buf, this.writerType)
    "?" + buf.toString.replaceAll(" ", "%20")
  }

}