/*
 * Copyright 2011 Kazuhiro Sera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

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
