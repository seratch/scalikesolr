package com.github.seratch.scalikesolr.request.query

import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.RequestParam

case class IsOmitHeaderEnabled(@BeanProperty val omitHeader: Boolean = false) extends RequestParam {

  override def isEmpty() = !omitHeader

  override def toQueryString() = "omitHeader=" + omitHeader

}


object IsOmitHeaderEnabled {
  def as(omitHeader: Boolean): IsOmitHeaderEnabled = new IsOmitHeaderEnabled(omitHeader)
}
