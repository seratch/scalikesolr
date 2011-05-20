package com.github.seratch.scalikesolr.request.query

import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.RequestParam

case class Version(@BeanProperty val version: String = "2.2") extends RequestParam {

  override def isEmpty() = version == null || version.isEmpty || version == "2.2"

  override def toQueryString() = "version=" + version

}

object Version {
  def as(version: String): Version = new Version(version)
}
