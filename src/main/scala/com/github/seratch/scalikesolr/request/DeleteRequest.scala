package com.github.seratch.scalikesolr.request

import common.WriterType
import query.Query
import reflect.BeanProperty

import com.github.seratch.scalikesolr.{SolrDocument, SolrCore}
import collection.JavaConverters._
import util.QueryStringUtil

case class DeleteRequest(@BeanProperty var core: SolrCore = SolrCore(),
                         var uniqueKeysToDelete: List[String] = Nil,
                         var queries: List[Query] = Nil,
                         @BeanProperty var writerType: WriterType = WriterType.Standard,
                         @BeanProperty var additionalQueryString: String = "") {

  def this() = {
    this (SolrCore(), Nil, Nil, WriterType.Standard, "")
  }

  def getUniqueKeysToDetelteInJava(): java.util.List[String] = uniqueKeysToDelete.asJava

  def setUniqueKeysToDetelteInJava(uniqueKeysToDelete: java.util.List[String]) = {
    this.uniqueKeysToDelete = uniqueKeysToDelete.asScala.toList
  }

  def getQueriesInJava(): java.util.List[Query] = queries.asJava

  def setQueriesInJava(queries: java.util.List[Query]): Unit = {
    this.queries = queries.asScala.toList
  }

  def toQueryString(): String = {
    val buf = new StringBuilder
    QueryStringUtil.appendIfExists(buf, writerType)
    "?" + buf.toString + additionalQueryString
  }

}