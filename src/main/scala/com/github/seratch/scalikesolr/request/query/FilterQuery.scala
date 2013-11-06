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

package com.github.seratch.scalikesolr.request.query

import scala.collection.JavaConverters._
import scala.beans.BeanProperty
import com.github.seratch.scalikesolr.request.common.RequestParam

case class FilterQuery(@BeanProperty val fq: String = "",
    @BeanProperty val multiple: Seq[String] = Nil) extends RequestParam {

  // for Java users
  def this(fq: String) = this(fq, Nil)

  // for Java users
  def this(juList: java.util.List[String]) = this("", juList.asScala)

  override def isEmpty() = (fq == null || fq.isEmpty) && multiple.isEmpty

  override def isMultiple() = !multiple.isEmpty

  override def getKey() = "fq"

  override def getValue() = toString(fq)

  override def getValues() = multiple

}

object FilterQuery {
  def as(fq: String): FilterQuery = new FilterQuery(fq)
  def as(multiple: Seq[String]): FilterQuery = new FilterQuery(multiple = multiple)
  def as(multiple: java.util.List[String]): FilterQuery = new FilterQuery(multiple = multiple.asScala)
}
