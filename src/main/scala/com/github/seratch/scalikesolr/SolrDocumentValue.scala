package com.github.seratch.scalikesolr

import scala.reflect.BeanProperty
import org.joda.time.{LocalDate, LocalTime, DateTime}

import collection.JavaConverters._
import util.JSONUtil
import java.util.{Calendar, Date}
import org.joda.time.format.DateTimeFormat
import java.text.SimpleDateFormat

case class SolrDocumentValue(@BeanProperty val rawValue: String) {

  def toListOrElse(defaultValue: List[String]): List[String] = {
    try {
      // List(book, hardcover)
      val values = rawValue.replaceFirst("List\\(", "").replaceFirst("\\)", "")
      values.split(",").toList
    } catch {
      case _ => defaultValue
    }
  }

  def toListInJavaOrElse(defaultValue: java.util.List[String]): java.util.List[String] = {
    try {
      toListOrElse(if (defaultValue == null) Nil else defaultValue.asScala.toList).asJava
    } catch {
      case _ => defaultValue
    }
  }

  def toDateOrElse(defaultValue: Date): Date = {
    try {
      try {
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(rawValue)
      } catch {
        case _ => new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(rawValue)
      }
    } catch {
      case _ => defaultValue
    }
  }

  def toCalendarOrElse(defaultValue: Calendar): Calendar = {
    try {
      val date = toDateOrElse(null)
      val cal = Calendar.getInstance()
      cal.setTime(date)
      cal
    } catch {
      case _ => defaultValue
    }
  }

  def toDateTimeOrElse(defaultValue: DateTime): DateTime = {
    try {
      try {
        DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parseDateTime(rawValue)
      } catch {
        case _ => DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").parseDateTime(rawValue)
      }
    } catch {
      case _ => defaultValue
    }
  }

  def toLocalTimeOrElse(defaultValue: LocalTime): LocalTime = {
    try {
      toDateTimeOrElse(null).toLocalTime
    } catch {
      case _ => defaultValue
    }
  }

  def toLocalDateOrElse(defaultValue: LocalDate): LocalDate = {
    try {
      toDateTimeOrElse(null).toLocalDate
    } catch {
      case _ => defaultValue
    }
  }

  def toBooleanOrElse(defaultValue: Boolean): Boolean = {
    try {
      rawValue.toBoolean
    } catch {
      case _ => defaultValue
    }
  }

  def toByteOrElse(defaultValue: Byte): Byte = {
    try {
      rawValue.toByte
    } catch {
      case _ => defaultValue
    }
  }

  def toFloatOrElse(defaultValue: Float): Float = {
    try {
      rawValue.toFloat
    } catch {
      case _ => defaultValue
    }
  }

  def toDoubleOrElse(defaultValue: Double): Double = {
    try {
      rawValue.toDouble
    } catch {
      case _ => defaultValue
    }
  }

  def toIntOrElse(defaultValue: Int): Int = {
    try {
      JSONUtil.normalizeNum(rawValue).toInt
    } catch {
      case _ => defaultValue
    }
  }

  def toLongOrElse(defaultValue: Long): Long = {
    try {
      rawValue.toLong
    } catch {
      case _ => defaultValue
    }
  }

  def toShortOrElse(defaultValue: Double): Double = {
    try {
      rawValue.toShort
    } catch {
      case _ => defaultValue
    }
  }

  def toNullableBooleanOrElse(defaultValue: java.lang.Boolean): java.lang.Boolean = {
    try {
      rawValue.toBoolean
    } catch {
      case _ => defaultValue
    }
  }

  def toNullableByteOrElse(defaultValue: java.lang.Byte): java.lang.Byte = {
    try {
      rawValue.toByte
    } catch {
      case _ => defaultValue
    }
  }

  def toNullableFloatOrElse(defaultValue: java.lang.Float): java.lang.Float = {
    try {
      rawValue.toFloat
    } catch {
      case _ => defaultValue
    }
  }

  def toNullableDoubleOrElse(defaultValue: java.lang.Double): java.lang.Double = {
    try {
      rawValue.toDouble
    } catch {
      case _ => defaultValue
    }
  }

  def toNullableIntOrElse(defaultValue: java.lang.Integer): java.lang.Integer = {
    try {
      JSONUtil.normalizeNum(rawValue).toInt
    } catch {
      case _ => defaultValue
    }
  }

  def toNullableLongOrElse(defaultValue: java.lang.Long): java.lang.Long = {
    try {
      rawValue.toLong
    } catch {
      case _ => defaultValue
    }
  }

  def toNullableShortOrElse(defaultValue: java.lang.Double): java.lang.Double = {
    try {
      rawValue.toShort
    } catch {
      case _ => defaultValue
    }
  }


  override def toString(): String = rawValue

}