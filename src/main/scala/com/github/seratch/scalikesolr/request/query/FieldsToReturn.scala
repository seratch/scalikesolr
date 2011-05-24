package com.github.seratch.scalikesolr.request.query

import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.RequestParam

case class FieldsToReturn(@BeanProperty val fl: String = "*") extends RequestParam {

  override def isEmpty() = fl == null || fl.isEmpty

  override def getKey() = "fl"

  override def getValue() = toString(fl)

}

object FieldsToReturn {
  def as(fl: String): FieldsToReturn = new FieldsToReturn(fl)
}
