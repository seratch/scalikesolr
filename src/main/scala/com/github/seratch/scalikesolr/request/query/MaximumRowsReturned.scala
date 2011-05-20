package com.github.seratch.scalikesolr.request.query

import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.RequestParam

case class MaximumRowsReturned(@BeanProperty val rows: Int = 10) extends RequestParam {

  override def isEmpty() = false

  override def toQueryString() = "rows=" + rows
}

object MaximumRowsReturned {
  def as(rows: Int): MaximumRowsReturned = new MaximumRowsReturned(rows)
}
