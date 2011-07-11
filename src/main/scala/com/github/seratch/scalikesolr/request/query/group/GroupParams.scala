/*
 * Copyright 2011 Kazuhiro Sera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.github.seratch.scalikesolr.request.query.group

import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.RequestParam
import com.github.seratch.scalikesolr.request.query.{Sort, StartRow, MaximumRowsReturned}

case class GroupParams(@BeanProperty var enabled: Boolean = false,
                       @BeanProperty var field: Field = Field(),
                       @BeanProperty var query: Query = Query(),
                       @BeanProperty var rows: MaximumRowsReturned = MaximumRowsReturned(),
                       @BeanProperty var start: StartRow = StartRow(),
                       @BeanProperty var limit: Limit = Limit(),
                       @BeanProperty var offset: Offset = Offset(),
                       @BeanProperty var sort: Sort = Sort(),
                       @BeanProperty var groupSort: GroupSort = GroupSort(),
                       @BeanProperty var format: Format = Format(),
                       @BeanProperty var main: AsMainResultWhenUsingSimpleFormat = AsMainResultWhenUsingSimpleFormat(),
                       @BeanProperty var ngroups: WithNumberOfGroups = WithNumberOfGroups(),
                       @BeanProperty var cachePercent: GroupingCachePercent = GroupingCachePercent()) {

  def this(enabled: Boolean, field: Field) {
    this (enabled = enabled,
      field = field,
      query = Query(),
      rows = MaximumRowsReturned(),
      start = StartRow(),
      limit = Limit(),
      offset = Offset(),
      sort = Sort(),
      groupSort = GroupSort(),
      format = Format(),
      main = AsMainResultWhenUsingSimpleFormat(),
      ngroups = WithNumberOfGroups(),
      cachePercent = GroupingCachePercent()
    )
  }

}

object GroupParams {
  def as(enabled: Boolean, field: Field) = {
    new GroupParams(enabled, field)
  }
}

case class Field(@BeanProperty val field: String = "*") extends RequestParam {

  override def isEmpty() = field == null || field.isEmpty

  override def getKey() = "group.field"

  override def getValue() = field.toString

}

object Field {
  def as(field: String) = Field(field)
}

/**
 * TODO This parameter only is supported on 4.0
 */
case class FunctionalQeury(@BeanProperty val func: String) extends RequestParam {

  override def isEmpty() = func == null || func.isEmpty

  override def getKey() = "group.func"

  override def getValue() = func.toString

}

object FunctionalQeury {
  def as(func: String) = FunctionalQeury(func)
}

case class Query(@BeanProperty val query: String = "") extends RequestParam {

  override def isEmpty() = query == null || query.isEmpty

  override def getKey() = "group.query"

  override def getValue() = query.toString

}

object Query {
  def as(query: String) = Query(query)
}

case class Limit(@BeanProperty val limit: Int = 1) extends RequestParam {

  override def isEmpty() = false

  override def getKey() = "group.limit"

  override def getValue() = limit.toString

}

object Limit {
  def as(limit: Int) = Limit(limit)
}

case class Offset(@BeanProperty val offset: Int = 0) extends RequestParam {

  override def isEmpty() = false

  override def getKey() = "group.offset"

  override def getValue() = offset.toString

}

object Offset {
  def as(offset: Int) = Offset(offset)
}

case class GroupSort(@BeanProperty val sort: String = "") extends RequestParam {

  override def isEmpty() = sort == null || sort.isEmpty

  override def getKey() = "group.sort"

  override def getValue() = toString(sort)

}

object GroupSort {
  def as(sort: String): GroupSort = GroupSort(sort)
}

case class Format(@BeanProperty val format: String = "grouped") extends RequestParam {

  override def isEmpty() = format == null || format.isEmpty

  override def getKey() = "group.format"

  override def getValue() = toString(format)

}

object Format {
  def as(format: String) = Format(format)
}

case class AsMainResultWhenUsingSimpleFormat(@BeanProperty val main: Boolean = false) extends RequestParam {

  override def isEmpty() = !main

  override def getKey() = "group.main"

  override def getValue() = toString(main)

}

object AsMainResultWhenUsingSimpleFormat {
  def as(main: Boolean) = AsMainResultWhenUsingSimpleFormat(main)
}

case class WithNumberOfGroups(@BeanProperty val ngroups: Boolean = false) extends RequestParam {

  override def isEmpty() = !ngroups

  override def getKey() = "group.ngroups"

  override def getValue() = toString(ngroups)

}

object WithNumberOfGroups {
  def as(ngroups: Boolean) = WithNumberOfGroups(ngroups)
}

case class GroupingCachePercent(@BeanProperty val cachePercent: Int = 0) extends RequestParam {

  override def isEmpty() = false

  override def getKey() = "group.cache.percent"

  override def getValue() = cachePercent.toString

}

object GroupingCachePercent {
  def as(cachePercent: Int) = GroupingCachePercent(cachePercent)
}
