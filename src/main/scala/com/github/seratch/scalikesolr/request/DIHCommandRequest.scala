package com.github.seratch.scalikesolr.request

import common.WriterType
import reflect.BeanProperty
import com.github.seratch.scalikesolr.SolrCore

case class DIHCommandRequest(@BeanProperty var core: SolrCore = SolrCore(),
                             @BeanProperty var writerType: WriterType = WriterType(),
                             @BeanProperty val command: String) {

  def this(command: String) {
    this (
      core = SolrCore(),
      command = command
    )
  }

  private val extraParams = new collection.mutable.HashMap[String, Any]

  def updateExtraParam(key: String, value: Any) = extraParams.update(key, value)

  def removeExtraParam(key: String) = extraParams.remove(key)

  def toQueryString: String = {
    val buf = new StringBuilder
    buf.append("command=")
    buf.append(command)
    if (extraParams.size > 0) {
      extraParams.keys.foreach {
        key => {
          if (buf.length > 0) buf.append("&")
          buf.append(key)
          buf.append("=")
          buf.append(extraParams.getOrElse(key, ""))
        }
      }
    }
    "?" + buf.toString
  }

}

object DIHCommandRequest {

  val FullImport = new DIHCommandRequest("full-import")

  val DeltaImport = new DIHCommandRequest("delta-import")

}