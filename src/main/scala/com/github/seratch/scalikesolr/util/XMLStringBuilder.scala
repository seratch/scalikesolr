package com.github.seratch.scalikesolr.util

import java.lang.StringBuilder

class XMLStringBuilder {

  val buf = new StringBuilder

  def append(str: String): Unit = buf.append(str)

  def appendEscaped(str: String): Unit = {
    buf.append(
      str
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll("\"", "&quot;")
        .replaceAll("'", "&apos;"))
  };

  def build(): String = buf.toString

  override def toString() = buf.toString

}