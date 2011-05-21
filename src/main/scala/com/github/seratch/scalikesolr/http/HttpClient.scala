package com.github.seratch.scalikesolr.http

import java.net.{URL, HttpURLConnection}
import java.io._
import com.github.seratch.scalikesolr.util.IO

object HttpClient {

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
    conn.connect()
    try {
      import collection.JavaConverters._
      val headersInJava = conn.getHeaderFields
      val mapBuffer = new collection.mutable.HashMap[String, List[String]]
      headersInJava.keySet.asScala.foreach {
        case key => mapBuffer.update(key, headersInJava.get(key).asScala.toList)
      }
      HttpResponse(
        conn.getResponseCode,
        mapBuffer.toMap,
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