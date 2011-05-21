package com.github.seratch.scalikesolr.request

import reflect.BeanProperty

import com.github.seratch.scalikesolr.{SolrDocument, SolrCore}

case class UpdateRequest(@BeanProperty var core: SolrCore = SolrCore(),
                         @BeanProperty var queryString: String = "") {

  def this() = {
    this (SolrCore())
  }

}