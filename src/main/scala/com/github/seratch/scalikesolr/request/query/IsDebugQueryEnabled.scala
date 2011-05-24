package com.github.seratch.scalikesolr.request.query

import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.RequestParam

case class IsDebugQueryEnabled(@BeanProperty val debugQuery: Boolean = false) extends RequestParam {

  override def isEmpty() = !debugQuery

  override def getKey() = "debugQuery"

  override def getValue() = toString(debugQuery)

}

object IsDebugQueryEnabled {
  def as(debugQuery: Boolean): IsDebugQueryEnabled = new IsDebugQueryEnabled(debugQuery)
}
