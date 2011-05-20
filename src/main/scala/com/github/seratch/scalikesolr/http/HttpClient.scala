package com.github.seratch.scalikesolr.http

import java.net.{URL, HttpURLConnection}
import java.io._

object HttpClient {

  def post(url: String, dataBinary: String, contentType: String, charset: String) = {

    val conn = new URL(url).openConnection().asInstanceOf[HttpURLConnection];
    conn.setConnectTimeout(3000)
    conn.setReadTimeout(10000)
    conn.setRequestMethod("POST")
    conn.setRequestProperty("Content-Type", contentType)
    conn.setRequestProperty("Content-Length", dataBinary.size.toString)

    var os: OutputStream = null
    var writer: OutputStreamWriter = null
    try {
      conn.setDoOutput(true);
      os = conn.getOutputStream();
      writer = new OutputStreamWriter(os, charset);
      writer.write(dataBinary);
    } finally {
      writer match {
        case null =>
        case _ => try writer.close catch {
          case _ =>
        }
      }
      os match {
        case null =>
        case _ => try os.close catch {
          case _ =>
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
        getResponseCotent(conn, charset))
    }
    catch {
      case e: IOException => throw e
    }

  }

  private def getResponseCotent(conn: HttpURLConnection, charset: String): String = {
    var is: InputStream = null
    var br: BufferedReader = null
    try {
      is = conn.getInputStream
      val isr = charset match {
        case null => new InputStreamReader(is)
        case _ => new InputStreamReader(is, charset)
      }
      br = new BufferedReader(isr)
      val buf = new StringBuilder
      var line: String = null
      while ( {
        line = br.readLine;
        line
      } != null) {
        buf.append(line)
        buf.append("\n")
      }
      buf.toString
    }
    finally {
      is match {
        case null =>
        case _ => try is.close catch {
          case _ =>
        }
      }
      br match {
        case null =>
        case _ => try br.close catch {
          case _ =>
        }
      }
    }
  }

}