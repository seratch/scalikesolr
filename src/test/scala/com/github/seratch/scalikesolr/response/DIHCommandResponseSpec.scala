package com.github.seratch.scalikesolr.response

import org.scalatest._
import org.scalatest.matchers._
import com.github.seratch.scalikesolr.request.common.WriterType
import com.github.seratch.scalikesolr._

class DIHCommandResponseSpec extends FlatSpec with ShouldMatchers {

  behavior of "DIHCommandResponse"

  it should "be available" in {
    val writerType: WriterType = WriterType.JSON
    val rawBody: String = """
{
  "responseHeader":{"status":0,"QTime":1},
  "initArgs":["defaults",["config","db-data-config.xml"]],
  "status":"idle",
  "importResponse":"",
  "statusMessages":{
    "Time Elapsed":"0:9:27.818",
    "Total Requests made to DataSource":"1",
    "Total Rows Fetched":"0",
    "Total Documents Processed":"0",
    "Total Documents Skipped":"0",
    "Delta Dump started":"2012-03-28 11:10:01",
    "Identifying Delta":"2012-03-28 11:10:01",
    "":"Indexing failed. Rolled back all changes.",
    "Rolledback":"2012-03-28 11:10:01"
  },
  "WARNING":"This response format is experimental.  It is likely to change in the future."
}
                          """
    val response = new DIHCommandResponse(writerType, rawBody)
    response should not be null
    response.initArgs.defaults should not be null
  }

}
