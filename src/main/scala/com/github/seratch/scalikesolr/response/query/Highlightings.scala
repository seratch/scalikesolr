package com.github.seratch.scalikesolr.response.query

import com.github.seratch.scalikesolr.SolrDocument
import reflect.BeanProperty
import java.lang.Iterable

case class Highlightings(@BeanProperty val highlightings: Map[String, SolrDocument]) {

  def keys(): List[String] = highlightings.keys.toList

  def get(name: String): SolrDocument = highlightings.getOrElse(name, new SolrDocument())

  def size(): Int = highlightings.size

}
