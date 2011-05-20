package com.github.seratch.scalikesolr.request.query

import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.RequestParam

case class ExplainOther(@BeanProperty val explainOther: String = "") extends RequestParam {

  override def isEmpty() = explainOther == null || explainOther.isEmpty

  override def toQueryString() = "explainOther=" + explainOther

}

object ExplainOther {
  def as(explainOther: String): ExplainOther = new ExplainOther(explainOther)
}
