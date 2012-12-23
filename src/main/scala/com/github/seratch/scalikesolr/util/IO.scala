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

import java.io.{ InputStreamReader, BufferedReader, InputStream }

object IO {

  def using[CLOSABLE <: { def close(): Unit }, RETURNED](resource: CLOSABLE)(func: CLOSABLE => RETURNED): RETURNED = {
    try {
      func(resource)
    } finally {
      resource match {
        case null =>
        case _ => try resource.close() catch {
          case _: Throwable =>
        }
      }
    }
  }

  def readAsString(is: InputStream, charset: String = "UTF-8"): String = {
    using(is) {
      is =>
        using(new BufferedReader(charset match {
          case null => new InputStreamReader(is)
          case _ => new InputStreamReader(is, charset)
        })) {
          br =>
            val buf = new StringBuilder
            var line: String = null
            while ({
              line = br.readLine;
              line
            } != null) {
              buf.append(line)
              buf.append("\n")
            }
            buf.toString
        }
    }
  }

}