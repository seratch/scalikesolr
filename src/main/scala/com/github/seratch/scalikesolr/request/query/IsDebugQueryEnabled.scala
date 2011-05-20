package com.github.seratch.scalikesolr.request.query

import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.RequestParam

case class IsDebugQueryEnabled(@BeanProperty val debugQuery: Boolean = false) extends RequestParam {

  override def isEmpty() = !debugQuery

  override def toQueryString() = "debugQuery=" + debugQuery

}

object IsDebugQueryEnabled {
  def as(debugQuery: Boolean): IsDebugQueryEnabled = new IsDebugQueryEnabled(debugQuery)
}
