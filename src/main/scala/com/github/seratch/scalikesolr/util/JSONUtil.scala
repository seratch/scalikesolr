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

package com.github.seratch.scalikesolr.util

object JSONUtil {

  def normalizeNum(num: String): String = num.replaceFirst("\\.0+$", "")

  def toMap(obj: Option[Any]): Map[String, Option[Any]] = {
    obj.getOrElse(Map()).asInstanceOf[Map[String, Option[Any]]]
  }

  def toList(obj: Option[Any]): List[Map[String, Option[Any]]] = {
    obj.getOrElse(Nil).asInstanceOf[List[Map[String, Option[Any]]]]
  }

}