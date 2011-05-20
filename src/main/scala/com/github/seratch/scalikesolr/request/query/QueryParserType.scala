package com.github.seratch.scalikesolr.request.query

import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.RequestParam

case class QueryParserType(@BeanProperty val defType: String = "lucene") extends RequestParam {

  override def isEmpty() = defType == null || defType.isEmpty || defType == "lucene"

  override def toQueryString() = "defType=" + defType

}

object QueryParserType {
  def as(defType: String): QueryParserType = new QueryParserType(defType)
}

