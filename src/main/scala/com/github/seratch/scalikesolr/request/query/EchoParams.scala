package com.github.seratch.scalikesolr.request.query

import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.RequestParam

case class EchoParams(@BeanProperty val echoParams: String = "none") extends RequestParam {

  override def isEmpty() = echoParams == null || echoParams.isEmpty || echoParams == "none"

  override def getKey() = "echoParams"

  override def getValue() = toString(echoParams)

}

object EchoParams {
  def as(echoParams: String): EchoParams = new EchoParams(echoParams)
}
