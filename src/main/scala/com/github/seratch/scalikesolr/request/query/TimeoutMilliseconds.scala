package com.github.seratch.scalikesolr.request.query

import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.RequestParam

case class TimeoutMilliseconds(@BeanProperty val timeAllowed: Long = 0) extends RequestParam {

  override def isEmpty() = timeAllowed <= 0

  override def toQueryString() = "timeAllowed=" + timeAllowed

}

object TimeoutMilliseconds {
  def as(timeAllowed: Long): TimeoutMilliseconds = new TimeoutMilliseconds(timeAllowed)
}
