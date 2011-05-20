package com.github.seratch.scalikesolr.request.query

import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.RequestParam

case class Query(@BeanProperty val q: String) extends RequestParam {

  override def isEmpty() = q == null || q.isEmpty

  override def toQueryString() = "q=" + q

}

object Query {
  def as(q: String): Query = new Query(q)
}
