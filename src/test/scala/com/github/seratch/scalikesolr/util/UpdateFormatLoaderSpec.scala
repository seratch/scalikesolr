package com.github.seratch.scalikesolr.util

import org.slf4j.LoggerFactory

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec

@RunWith(classOf[JUnitRunner])
class UpdateFormatLoaderSpec extends FlatSpec with ShouldMatchers {

  behavior of "UpdateFormatLoader"

  val log = LoggerFactory.getLogger("com.github.seratch.scalikesolr.util.UpdateFormatLoaderSpec")

  "fromXMLString" should "be available" in {
    val xmlString = "<add><doc><field name=\"employeeId\">05991</field><field name=\"office\">Bridgewater</field><field name=\"skills\">Perl</field><field name=\"skills\">Java</field></doc><doc><field name=\"employeeId\">05992</field><field name=\"office\">Charles</field><field name=\"skills\">Ruby</field></doc></add>"
    val docs = UpdateFormatLoader.fromXMLString(xmlString)
    docs.size should equal(2)
    log.debug("-----------------------------")
    log.debug(docs.toString)
    docs foreach {
      case doc => {
        log.debug("employeeId:" + doc.get("employeeId").toString()) // "05991"
        log.debug("office:" + doc.get("office").toString()) // "Bridgewater"
      }
    }
  }

  "fromCSVString" should "be available" in {
    val csvString = "id,cat,name,price,inStock,author_t,series_t,sequence_i,genre_s\r\n0553573403,book,A Game of Thrones,7.99,true,George R.R. Martin,\"A Song of Ice and Fire\",1,fantasy\r\n0553579908,book,A Clash of Kings,7.99,true,George R.R. Martin,\"A Song of Ice and Fire\",2,fantasy\n"
    val docs = UpdateFormatLoader.fromCSVString(csvString)
    docs.size should equal(2)
    log.debug("-----------------------------")
    log.debug(docs.toString)
    docs foreach {
      case doc => {
        log.debug("id:" + doc.get("id").toString()) // "0553573403"
        log.debug("name:" + doc.get("name").toString()) // "A Game of Thrones"
        log.debug("sequence_i:" + doc.get("sequence_i").toIntOrElse(0)) // 1
      }
    }
  }

}