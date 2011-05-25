package com.github.seratch.scalikesolr.util

import xml.{Node, XML}
import com.github.seratch.scalikesolr.request.common.WriterType
import com.github.seratch.scalikesolr.{SolrDocumentValue, SolrDocument}
import java.io.{FileInputStream, File}

import collection.JavaConverters._

object UpdateFormatLoader {

  def fromXML(xml: File): List[SolrDocument] = {
    IO.using(new FileInputStream(xml)) {
      fis => {
        val xmlData = XML.load(fis)
        (xmlData \\ "doc").map({
          case doc: Node => new SolrDocument(writerType = WriterType.Standard, rawBody = doc.toString)
        }).toList
      }
    }
  }

  def fromXMLInJava(xml: File): java.util.List[SolrDocument] = {
    fromXML(xml).asJava
  }

  def fromXMLString(xmlString: String): List[SolrDocument] = {
    val xmlData = XML.loadString(xmlString)
    (xmlData \\ "doc").map({
      case doc: Node => new SolrDocument(writerType = WriterType.Standard, rawBody = doc.toString)
    }).toList
  }

  def fromXMLStringInJava(xmlString: String): java.util.List[SolrDocument] = {
    fromXMLString(xmlString).asJava
  }

  def fromCSV(csv: File): List[SolrDocument] = {
    val csvString = IO.readAsString(new FileInputStream(csv), "UTF-8")
    fromCSVString(csvString)
  }

  def fromCSVInJava(csv: File): java.util.List[SolrDocument] = {
    fromCSV(csv).asJava
  }

  def fromCSVString(csvString: String): List[SolrDocument] = {
    var headers: List[String] = Nil
    val listBuf = new collection.mutable.ListBuffer[SolrDocument]
    csvString.split("\n") foreach ({
      case line: String if headers == Nil => {
        headers = line.replaceFirst("\r", "").split(",").toList
      }
      case line: String => {
        val values = line.replaceFirst("\r", "").split(",").toList
        val docMap = (headers.zip(values).toList map {
          case (key, value) => {
            (key.toString, new SolrDocumentValue(value.toString))
          }
        }).toMap
        listBuf.append(new SolrDocument(map = docMap))
      }
    })
    listBuf.toList
  }

  def fromCSVStringInJava(csvString: String): java.util.List[SolrDocument] = {
    fromCSVString(csvString).asJava
  }

}