package com.github.seratch.scalikesolr.request.common

trait RequestParam {

  def isEmpty(): Boolean

  def toQueryString(): String

}