package com.github.seratch.scalikesolr.util

object JSONUtil {

  def normalizeNum(num: String) = num.replaceFirst("\\.0+$", "")

  def toMap(obj: Option[Any]): Map[String, Option[Any]] = obj.getOrElse(Map()).asInstanceOf[Map[String, Option[Any]]]

  def toList(obj: Option[Any]): List[Map[String, Option[Any]]] = obj.getOrElse(Nil).asInstanceOf[List[Map[String, Option[Any]]]]

}