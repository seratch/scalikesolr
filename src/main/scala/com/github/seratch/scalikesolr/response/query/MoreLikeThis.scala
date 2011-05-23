package com.github.seratch.scalikesolr.response.query

import com.github.seratch.scalikesolr.SolrDocument
import reflect.BeanProperty

import collection.JavaConverters._

case class MoreLikeThis(@BeanProperty val numFound: Int = 0,
                        @BeanProperty val start: Int = 0,
                        @BeanProperty val idAndRecommendations: Map[String, List[SolrDocument]]) {

  def getList(name: String): List[SolrDocument] = idAndRecommendations.getOrElse(name, Nil)

  def getListInJava(name: String): java.util.List[SolrDocument] = getList(name).asJava

}
