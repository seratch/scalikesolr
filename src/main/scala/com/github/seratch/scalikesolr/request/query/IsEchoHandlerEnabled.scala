package com.github.seratch.scalikesolr.request.query

import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.RequestParam

case class IsEchoHandlerEnabled(@BeanProperty val echoHandler: Boolean = false) extends RequestParam {

  override def isEmpty() = !echoHandler

  override def toQueryString() = "echoHandler=" + echoHandler

}

object IsEchoHandlerEnabled {
  def as(echoHandler: Boolean): IsEchoHandlerEnabled = new IsEchoHandlerEnabled(echoHandler)
}
