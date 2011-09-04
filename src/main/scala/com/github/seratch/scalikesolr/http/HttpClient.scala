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
import org.apache.solr.common.util.{NamedList, JavaBinCodec}
import reflect.BeanProperty

object HttpClient {
  val DEFAULT_CONNECT_TIMEOUT_MILLIS = 3000
  val DEFAULT_READ_TIMEOUT_MILLIS = 10000
}

class HttpClient(@BeanProperty val connectTimeout: Int = HttpClient.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                 @BeanProperty val readTimeout: Int = HttpClient.DEFAULT_READ_TIMEOUT_MILLIS) {

  def getAsJavabin(url: String): JavabinHttpResponse = {
    val conn = new URL(url).openConnection().asInstanceOf[HttpURLConnection];
    conn.setConnectTimeout(connectTimeout)
    conn.setReadTimeout(readTimeout)
    conn.setRequestMethod("GET")
    try {
      val headersInJava = conn.getHeaderFields
      val headers = for ((k, v) <- headersInJava.asScala.toMap) yield (k -> v.asScala.toList)
      val response: NamedList[Any] = new JavaBinCodec().unmarshal(conn.getInputStream).asInstanceOf[NamedList[Any]]
      new JavabinHttpResponse(
        conn.getResponseCode,
        headers,
        response
      )
    }
    catch {
      case e: IOException => throw e
    }
  }

  def get(url: String, charset: String): HttpResponse = {

    val conn = new URL(url).openConnection().asInstanceOf[HttpURLConnection];
    conn.setConnectTimeout(connectTimeout)
    conn.setReadTimeout(readTimeout)
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
    conn.setConnectTimeout(connectTimeout)
    conn.setReadTimeout(readTimeout)
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