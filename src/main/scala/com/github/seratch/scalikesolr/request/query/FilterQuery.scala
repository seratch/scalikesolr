package com.github.seratch.scalikesolr.request.query

import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.RequestParam

case class FilterQuery(@BeanProperty val fq: String = "") extends RequestParam {

  override def isEmpty() = fq == null || fq.isEmpty

  override def getKey() = "fq"

  override def getValue() = toString(fq)

}

object FilterQuery {
  def as(fq: String): FilterQuery = new FilterQuery(fq)
}
