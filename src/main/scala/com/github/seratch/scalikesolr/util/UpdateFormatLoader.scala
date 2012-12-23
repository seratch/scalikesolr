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

import scala.xml.{ Node, XML }
import com.github.seratch.scalikesolr.request.common.WriterType
import com.github.seratch.scalikesolr.{ SolrDocumentValue, SolrDocument }
import java.io.{ FileInputStream, File }
import scala.collection.JavaConverters._

object UpdateFormatLoader {

  def fromXML(xml: File): List[SolrDocument] = IO.using(new FileInputStream(xml)) {
    fis =>
      val xmlData = XML.load(fis)
      (xmlData \\ "doc").map {
        case doc: Node => new SolrDocument(writerType = WriterType.Standard, rawBody = doc.toString)
      }.toList
  }

  def fromXMLInJava(xml: File): java.util.List[SolrDocument] = fromXML(xml).asJava

  def fromXMLString(xmlString: String): List[SolrDocument] = {
    val xmlData = XML.loadString(xmlString)
    (xmlData \\ "doc").map {
      case doc: Node => new SolrDocument(writerType = WriterType.Standard, rawBody = doc.toString)
    }.toList
  }

  def fromXMLStringInJava(xmlString: String): java.util.List[SolrDocument] = fromXMLString(xmlString).asJava

  def fromCSV(csv: File): List[SolrDocument] = {
    val csvString = IO.readAsString(new FileInputStream(csv), "UTF-8")
    fromCSVString(csvString)
  }

  def fromCSVInJava(csv: File): java.util.List[SolrDocument] = fromCSV(csv).asJava

  def fromCSVString(csvString: String): List[SolrDocument] = {
    var headers: List[String] = Nil
    val listBuf = new collection.mutable.ListBuffer[SolrDocument]
    csvString.split("\n") foreach {
      case line: String if headers == Nil =>
        headers = line.replaceFirst("\r", "").split(",").toList
      case line: String =>
        val values = line.replaceFirst("\r", "").split(",").toList
        val docMap = headers.zip(values).toList.map {
          case (key, value) => (key.toString, new SolrDocumentValue(value.toString))
        }.toMap
        listBuf.append(new SolrDocument(map = docMap))
    }
    listBuf.toList
  }

  def fromCSVStringInJava(csvString: String): java.util.List[SolrDocument] = fromCSVString(csvString).asJava

}