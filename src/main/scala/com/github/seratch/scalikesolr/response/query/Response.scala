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

import reflect.BeanProperty
import com.github.seratch.scalikesolr.SolrDocument

case class Response(@BeanProperty val numFound: Int,
                    @BeanProperty val start: Int,
                    @BeanProperty val documents: List[SolrDocument]) {

  def getDocumentsInJava(): java.util.List[SolrDocument] = java.util.Arrays.asList(documents.toArray: _*)

}
