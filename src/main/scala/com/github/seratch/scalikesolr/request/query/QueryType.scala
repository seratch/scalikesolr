package com.github.seratch.scalikesolr.request.query

import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.RequestParam

case class QueryType(@BeanProperty val qt: String = "") extends RequestParam {

  override def isEmpty() = qt == null || qt.isEmpty

  override def getKey() = "qt"

  override def getValue() = toString(qt)

}

object QueryType {
  def as(qt: String): QueryType = new QueryType(qt)
}
