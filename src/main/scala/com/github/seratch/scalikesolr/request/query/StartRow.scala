package com.github.seratch.scalikesolr.request.query

import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.RequestParam

case class StartRow(@BeanProperty val start: Int = 0) extends RequestParam {

  override def isEmpty() = false

  override def getKey() = "start"

  override def getValue() = toString(start)

}

object StartRow {
  def as(start: Int): StartRow = new StartRow(start)
}
