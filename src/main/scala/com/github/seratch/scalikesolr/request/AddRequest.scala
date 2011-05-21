package com.github.seratch.scalikesolr.request

import reflect.BeanProperty

import com.github.seratch.scalikesolr.{SolrDocument, SolrCore}
import collection.JavaConverters._
import util.QueryStringUtil

case class AddRequest(@BeanProperty var core: SolrCore = SolrCore(),
                      var documents: List[SolrDocument] = Nil) {

  def this() = {
    this (SolrCore(), Nil)
  }

  def getDocumentsInJava(): java.util.List[SolrDocument] = documents.asJava

  def setDocumentsInJava(newDocuments: java.util.List[SolrDocument]) = {
    this.documents = newDocuments.asScala.toList
  }

}