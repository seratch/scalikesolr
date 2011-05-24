package com.github.seratch.scalikesolr.request.common

import reflect.BeanProperty

case class WriterType(@BeanProperty val wt: String = "standard") extends RequestParam {

  override def isEmpty() = wt == null || wt.isEmpty

  override def getKey() = "wt"

  override def getValue() = wt

}

object WriterType {

  def as(wt: String) = new WriterType(wt)

  val Standard = new WriterType("standard")

  val JSON = new WriterType("json")

}
