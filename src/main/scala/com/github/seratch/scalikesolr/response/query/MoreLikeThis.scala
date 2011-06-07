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

import collection.JavaConverters._

case class MoreLikeThis(@BeanProperty val numFound: Int = 0,
                        @BeanProperty val start: Int = 0,
                        @BeanProperty val idAndRecommendations: Map[String, List[SolrDocument]]) {

  def getList(name: String): List[SolrDocument] = idAndRecommendations.getOrElse(name, Nil)

  def getListInJava(name: String): java.util.List[SolrDocument] = getList(name).asJava

}
