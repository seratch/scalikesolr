package com.github.seratch.scalikesolr.util

import com.github.seratch.scalikesolr.SolrDocument
import org.joda.time.{LocalDate, DateTime}
import java.util.{Date, Calendar}
import org.slf4j.LoggerFactory
import util.matching.Regex
import java.lang.reflect.Modifier

object TypeBinder {

  val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.util.TypeBinder")

  def getSetterRegex(): Regex = "(.+)_\\$eq".r

  def getSetterRegexInJava(): Regex = "set(.+)".r

  def getSolrFieldName(name: String): String = toSnakeCase(name)

  def getSolrFieldNameInJava(name: String): String = toSnakeCase(name).substring(1)

  def bind[T](document: SolrDocument, clazz: Class[T]): T = {
    _bind(document, clazz, false)
  }

  def bindInJava[T](document: SolrDocument, clazz: Class[T]): T = {
    _bind(document, clazz, true)
  }

  private def _bind[T](document: SolrDocument, clazz: Class[T], isJava: Boolean): T = {
    val dest = clazz.newInstance
    val setter = if (isJava) getSetterRegexInJava() else getSetterRegex()
    val methods = clazz.getDeclaredMethods
    methods.foreach {
      case method if Modifier.isPublic(method.getModifiers) && !Modifier.isStatic(method.getModifiers) => {
        method.getName match {
          case setter(name) => {
            try {
              val argType = method.getParameterTypes.apply(0)
              val solrFieldName = if (isJava) getSolrFieldNameInJava(name) else getSolrFieldName(name)
              argType match {
                case t if t == classOf[List[_]] =>
                  method.invoke(dest, document.get(solrFieldName).toListOrElse(Nil))
                case t if t == classOf[java.util.List[_]] =>
                  method.invoke(dest, document.get(solrFieldName).toListInJavaOrElse(null))
                case t if t == classOf[Date] =>
                  method.invoke(dest, document.get(solrFieldName).toDateOrElse(null))
                case t if t == classOf[Calendar] =>
                  method.invoke(dest, document.get(solrFieldName).toCalendarOrElse(null))
                case t if t == classOf[DateTime] =>
                  method.invoke(dest, document.get(solrFieldName).toDateTimeOrElse(null))
                case t if t == classOf[LocalDate] =>
                  method.invoke(dest, document.get(solrFieldName).toLocalDateOrElse(null))
                case t if t == classOf[Boolean] =>
                  method.invoke(dest, document.get(solrFieldName).toNullableBooleanOrElse(false))
                case t if t == classOf[Float] =>
                  method.invoke(dest, document.get(solrFieldName).toNullableFloatOrElse(0.0F))
                case t if t == classOf[Double] =>
                  method.invoke(dest, document.get(solrFieldName).toNullableDoubleOrElse(0.0D))
                case t if t == classOf[Int] =>
                  method.invoke(dest, document.get(solrFieldName).toNullableIntOrElse(0))
                case t if t == classOf[Long] =>
                  method.invoke(dest, document.get(solrFieldName).toNullableLongOrElse(0))
                case t if t == classOf[Short] =>
                  method.invoke(dest, document.get(solrFieldName).toNullableShortOrElse(0))
                case t if t == classOf[String] =>
                  method.invoke(dest, document.get(solrFieldName).toString())
                case t => {
                  val constructor = t.getConstructor(classOf[String])
                  val instance = constructor.newInstance(document.get(solrFieldName).toString)
                  method.invoke(dest, instance.asInstanceOf[AnyRef])
                }
              }
            } catch {
              case e => log.warn("Cannot bind the type : " + name, e)
            }
          }
          case _ =>
        }
      }
      case _ =>
    }
    dest
  }

  def toSnakeCase(camelCase: String): String = {
    camelCase.toCharArray.toList.map({
      case c if Character.isUpperCase(c) => "_" + Character.toLowerCase(c)
      case c => c
    }).mkString
  }

}