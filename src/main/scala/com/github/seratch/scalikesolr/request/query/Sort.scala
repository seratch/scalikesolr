package com.github.seratch.scalikesolr.request.query

import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.RequestParam

case class Sort(@BeanProperty val sort: String = "") extends RequestParam {

  override def isEmpty() = sort == null || sort.isEmpty

  override def toQueryString() = "sort=" + sort

}

object Sort {
  def as(sort: String): Sort = new Sort(sort)
}
