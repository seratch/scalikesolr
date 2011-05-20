package com.github.seratch.scalikesolr

import reflect.BeanProperty
import util.JSONUtil
import xml.{Node, XML}
import scala.util.parsing.json.JSON

case class SolrDocument(@BeanProperty val writerType: WriterType = WriterType.Standard,
                        @BeanProperty val rawBody: String = "",
                        @BeanProperty val map: Map[String, SolrDocumentValue] = Map()) {

  def this(writerType: WriterType, rawBody: String) = {
    this (writerType = writerType, rawBody = rawBody, map = Map())
  }

  def this(writerType: WriterType, map: Map[String, SolrDocumentValue]) = {
    this (writerType = writerType, rawBody = "", map = map)
  }

  private lazy val document: Map[String, SolrDocumentValue] = {
    if (map != null && map.size > 0) {
      map
    } else {
      val mutableMap = new collection.mutable.HashMap[String, SolrDocumentValue]
      writerType match {
        case WriterType.Standard => {
          XML.loadString(rawBody).child foreach {
            case elem: Node => mutableMap.update((elem \ "@name").toString, new SolrDocumentValue(elem.text))
          }
        }
        case WriterType.JSON => {
          val jsonMap: Map[String, Option[Any]] = JSON.parseFull(rawBody).getOrElse(Map()).asInstanceOf[Map[String, Option[Any]]]
          jsonMap.keys.foreach {
            case key => {
              val value = JSONUtil.normalizeNum(jsonMap.get(key).getOrElse("").toString)
              mutableMap.update(key, new SolrDocumentValue(value))
            }
          }
        }
        case other => throw new UnsupportedOperationException("\"" + other.wt + "\" is currently not supported.")
      }
      mutableMap.toMap
    }
  }

  def keys(): List[String] = document.keys.toList

  def get(key: String): SolrDocumentValue = {
    val value = document.getOrElse(key, SolrDocumentValue(""))
    value
  }

}
