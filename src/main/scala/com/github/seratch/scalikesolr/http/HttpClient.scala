package com.github.seratch.scalikesolr.http

import java.net.{URL, HttpURLConnection}
import java.io._
import com.github.seratch.scalikesolr.util.IO
object HttpClient {

  def get(url: String, charset: String) = {

    val conn = new URL(url).openConnection().asInstanceOf[HttpURLConnection];
    conn.setConnectTimeout(3000)
    conn.setReadTimeout(10000)
    conn.setRequestMethod("GET")
    try {
      import collection.JavaConverters._
      val headersInJava = conn.getHeaderFields
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


  def post(url: String, dataBinary: String, contentType: String, charset: String) = {

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
      import collection.JavaConverters._
      val headersInJava = conn.getHeaderFields
      val headers = (headersInJava.keySet.asScala map {
        case key => (key, headersInJava.get(key).asScala.toList)
      }).toMap
      new HttpResponse(
        conn.getResponseCode,
        headers,
        getResponseContent(conn, charset))
    }
    catch {
      case e: IOException => throw e
    }

  }

  private def getResponseContent(conn: HttpURLConnection, charset: String): String = {
    IO.readAsString(conn.getInputStream, charset)
  }

}