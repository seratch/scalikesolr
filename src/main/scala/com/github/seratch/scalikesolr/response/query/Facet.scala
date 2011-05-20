package com.github.seratch.scalikesolr.response.query

import com.github.seratch.scalikesolr.SolrDocument
import reflect.BeanProperty

case class Facet(@BeanProperty val facetQueries: Map[String, SolrDocument],
                 @BeanProperty val facetFields: Map[String, SolrDocument],
                 @deprecated @BeanProperty val facetDates: Map[String, SolrDocument],
                 @BeanProperty val facetRanges: Map[String, SolrDocument]) {

  def getFromFacetFields(name: String): SolrDocument = facetFields.getOrElse(name, null)

  @deprecated def getFromFacetDates(date: String): SolrDocument = facetDates.getOrElse(date, null)

  def getFromFacetRanges(range: String): SolrDocument = facetRanges.getOrElse(range, null)

}
