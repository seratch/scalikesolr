package com.github.seratch.scalikesolr.response.query

import com.github.seratch.scalikesolr.SolrDocument
import reflect.BeanProperty

case class Highlightings(@BeanProperty val highlightings: Map[String, SolrDocument]) {

  def get(name: String): SolrDocument = highlightings.getOrElse(name, null)

  def size(): Int = highlightings.size

}
