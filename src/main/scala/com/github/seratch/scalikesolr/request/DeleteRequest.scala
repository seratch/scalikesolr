package com.github.seratch.scalikesolr.request

import query.Query
import reflect.BeanProperty

import com.github.seratch.scalikesolr.{SolrDocument, SolrCore}
import collection.JavaConverters._

case class DeleteRequest(@BeanProperty var core: SolrCore = SolrCore(),
                         var uniqueKeysToDelete: List[String] = Nil,
                         var queries: List[Query] = Nil) {

  def this() = {
    this (SolrCore(), Nil)
  }

  def getUniqueKeysToDetelteInJava(): java.util.List[String] = uniqueKeysToDelete.asJava

  def setUniqueKeysToDetelteInJava(uniqueKeysToDelete: java.util.List[String]) = {
    this.uniqueKeysToDelete = uniqueKeysToDelete.asScala.toList
  }

  def getQueriesInJava(): java.util.List[Query] = queries.asJava

  def setQueriesInJava(queries: java.util.List[Query]): Unit = {
    this.queries = queries.asScala.toList
  }

}