package com.github.seratch.scalikesolr.request.common

trait RequestParam {

  def isEmpty(): Boolean

  def getKey(): String

  def getValue(): String

  protected def toString(value: Any): String = {
    if (value != null) value.toString else null
  }
}