package com.github.seratch.scalikesolr.response.query

import com.github.seratch.scalikesolr.SolrDocument
import reflect.BeanProperty

case class MoreLikeThis(@BeanProperty val moreLikeThis: Map[String, SolrDocument]) {

  def get(name: String): SolrDocument = moreLikeThis.getOrElse(name, null)

}
