package com.github.seratch.scalikesolr

import reflect.BeanProperty

case class SolrCore(@BeanProperty val name: String = "")

object SolrCore {
  def as(name: String) = SolrCore(name)
}