package com.github.seratch.scalikesolr.request

import common.WriterType
import reflect.BeanProperty

import com.github.seratch.scalikesolr.{SolrDocument, SolrCore}
import collection.JavaConverters._
import util.QueryStringUtil

case class AddRequest(@BeanProperty var core: SolrCore = SolrCore(),
                      var documents: List[SolrDocument] = Nil,
                      @BeanProperty var writerType: WriterType = WriterType.Standard,
                      @BeanProperty var additionalQueryString: String = "") {

  def this() = {
    this (SolrCore(), Nil, WriterType.Standard, "")
  }

  def getDocumentsInJava(): java.util.List[SolrDocument] = documents.asJava

  def setDocumentsInJava(newDocuments: java.util.List[SolrDocument]) = {
    this.documents = newDocuments.asScala.toList
  }

  def toQueryString(): String = {
    val buf = new StringBuilder
    QueryStringUtil.appendIfExists(buf, writerType)
    "?" + buf.toString + additionalQueryString
  }

}