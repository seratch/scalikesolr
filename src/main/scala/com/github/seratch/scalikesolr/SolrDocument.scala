package com.github.seratch.scalikesolr

import reflect.BeanProperty
import util.{TypeBinder, JSONUtil}
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

  private lazy val jsonMapFromRawBody: Map[String, Option[Any]] = {
    writerType match {
      case WriterType.JSON => JSONUtil.toMap(JSON.parseFull(rawBody))
      case _ => Map()
    }
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
          jsonMapFromRawBody.keys.foreach {
            case key => {
              val value = JSONUtil.normalizeNum(jsonMapFromRawBody.get(key).getOrElse("").toString)
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

  def get(key: String): SolrDocumentValue = document.getOrElse(key, SolrDocumentValue(""))

  def bind[T](clazz: Class[T]): T = TypeBinder.bind(this, clazz)

  def bindInJava[T](clazz: Class[T]): T = TypeBinder.bindInJava(this, clazz)

}
