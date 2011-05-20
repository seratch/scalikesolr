package com.github.seratch.scalikesolr.request.query

import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.RequestParam

case class IsIndentEnabled(@BeanProperty val indent: Boolean = false) extends RequestParam {

  override def isEmpty() = !indent

  override def toQueryString() = "isIndentEnabled=" + (if (indent) "on" else "off")

}

object IsIndentEnabled {
  def as(indent: Boolean): IsIndentEnabled = new IsIndentEnabled(indent)
}
