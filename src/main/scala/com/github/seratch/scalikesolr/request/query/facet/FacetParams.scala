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

package com.github.seratch.scalikesolr.request.query.facet

import reflect.BeanProperty
import java.lang.StringBuilder
import java.net.URLEncoder

case class FacetParams(@BeanProperty var enabled: Boolean = false,
    var params: List[FacetParam] = Nil) {

  def this(enabled: Boolean) {
    this(enabled, Nil)
  }

  import collection.JavaConverters._

  def setParamsInJava(paramsInJava: java.util.List[FacetParam]): Unit = {
    params = paramsInJava.asScala.toList
  }

  def getParamsInJava(): java.util.List[FacetParam] = params.asJava

  override def toString(): String = {
    val buf = new StringBuilder
    if (this.enabled) {
      buf.append("facet=true")
      params foreach {
        param =>
          buf.append("&")
          if (!param.field.field.isEmpty) {
            buf.append("f.")
            buf.append(param.field.field)
            buf.append(".")
          }
          buf.append(param.param.param)
          buf.append("=")
          buf.append(URLEncoder.encode(param.value.value, "UTF-8"))
      }
    }
    buf.toString
  }

  def toQueryString(): String = toString

}

object FacetParams {
  def as(enabled: Boolean): FacetParams = new FacetParams(enabled)
}

case class FacetParam(val field: Field = Field(""), val param: Param, val value: Value) {

  def this(param: Param, value: Value) = {
    this(Field(""), param, value)
  }

}

case class Field(@BeanProperty val field: String)

case class Param(@BeanProperty val param: String)

case class Value(@BeanProperty val value: String)
