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

package com.github.seratch.scalikesolr.http

import java.net.{URL, HttpURLConnection}
import java.io._
import com.github.seratch.scalikesolr.util.IO
import collection.JavaConverters._

object HttpClient {

  def get(url: String, charset: String): HttpResponse = {

    val conn = new URL(url).openConnection().asInstanceOf[HttpURLConnection];
    conn.setConnectTimeout(3000)
    conn.setReadTimeout(10000)
    conn.setRequestMethod("GET")
    try {
      val headersInJava = conn.getHeaderFields
      // (k -> v) is a pair tuple which is same as (k,v).
      val headers = for ((k, v) <- headersInJava.asScala.toMap) yield (k -> v.asScala.toList)
      new HttpResponse(
        conn.getResponseCode,
        headers,
        getResponseContent(conn, charset))
    }
    catch {
      case e: IOException => throw e
    }

  }

  private val POST_CONTENT_TYPE = "application/x-www-form-urlencoded";

  def post(url: String, dataBinary: String, charset: String): HttpResponse = {
    post(url, dataBinary, POST_CONTENT_TYPE, charset)
  }

  def post(url: String, dataBinary: String, contentType: String, charset: String): HttpResponse = {

    val conn = new URL(url).openConnection().asInstanceOf[HttpURLConnection];
    conn.setConnectTimeout(3000)
    conn.setReadTimeout(10000)
    conn.setRequestMethod("POST")
    conn.setRequestProperty("Content-Type", contentType)
    conn.setRequestProperty("Content-Length", dataBinary.size.toString)
    conn.setDoOutput(true);
    IO.using(conn.getOutputStream) {
      os => {
        IO.using(new OutputStreamWriter(os, charset)) {
          writer => writer.write(dataBinary)
        }
      }
    }
    try {
      val headersInJava = conn.getHeaderFields
      val headers = for ((k, v) <- headersInJava.asScala.toMap) yield (k -> v.asScala.toList)
      new HttpResponse(
        conn.getResponseCode,
        headers,
        getResponseContent(conn, charset)
      )
    }
    catch {
      case e: IOException => throw e
    }

  }

  private def getResponseContent(conn: HttpURLConnection, charset: String): String = {
    IO.readAsString(conn.getInputStream, charset)
  }

}