package com.github.seratch.scalikesolr.request.query.facet

import reflect.BeanProperty
import java.lang.StringBuilder
import java.net.URLEncoder

case class FacetParams(@BeanProperty var enabled: Boolean = false,
                       @BeanProperty var params: List[FacetParam] = Nil) {

  override def toString(): String = {
    val buf = new StringBuilder
    if (this.enabled) {
      buf.append("facet=true")
      params foreach {
        param => {
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
    }
    buf.toString
  }

  def toQueryString(): String = toString()

}

object FacetParams {
  def as(enabled: Boolean): FacetParams = new FacetParams(enabled)
}

case class FacetParam(val field: Field = Field(""), val param: Param, val value: Value) {

  def this(param: Param, value: Value) = {
    this (Field(""), param, value)
  }

}

case class Field(@BeanProperty val field: String)

case class Param(@BeanProperty val param: String)

case class Value(@BeanProperty val value: String)
