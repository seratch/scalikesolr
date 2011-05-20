package com.github.seratch.scalikesolr.response.query

import reflect.BeanProperty
import com.github.seratch.scalikesolr.SolrDocument

case class Response(@BeanProperty val numFound: Int,
                    @BeanProperty val start: Int,
                    @BeanProperty val documents: List[SolrDocument]) {

  def getDocumentsInJava(): java.util.List[SolrDocument] = java.util.Arrays.asList(documents.toArray: _*)

}
