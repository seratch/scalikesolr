package com.github.seratch.scalikesolr.util

import java.io.{InputStreamReader, BufferedReader, InputStream}

object IO {

  def using[CLOSABLE <: {def close() : Unit}, RETURNED](resource: CLOSABLE)(func: CLOSABLE => RETURNED): RETURNED = {
    try {
      func(resource)
    } finally {
      resource match {
        case null =>
        case _ => try resource.close() catch {
          case _ =>
        }
      }
    }
  }

  def readAsString(is: InputStream, charset: String): String = {

    println("-------------" + charset)

    using(is) {
      is => {
        using({
          new BufferedReader(charset match {
            case null => new InputStreamReader(is)
            case _ => new InputStreamReader(is, charset)
          })
        }) {
          br => {
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
        }
      }
    }
  }

}