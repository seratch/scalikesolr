package com.github.seratch.scalikesolr.request

import org.scalatest._
import org.scalatest.matchers._
import common._
import query._
import com.github.seratch.scalikesolr.SolrCore

class QueryRequestSpec extends FlatSpec with ShouldMatchers {

  behavior of "QueryRequest"

  it should "be available" in {
    val core: SolrCore = null
    val explainOther: ExplainOther = null
    val fieldsToReturn: FieldsToReturn = null
    val filterQuery: FilterQuery = null
    val isIndentEnabled: IsIndentEnabled = null
    val isDebugQueryEnabled: IsDebugQueryEnabled = null
    val isEchoHandlerEnabled: IsEchoHandlerEnabled = null
    val isOmitHeaderEnabled: IsOmitHeaderEnabled = null
    val maximumRowsReturned: MaximumRowsReturned = null
    val query: Query = null
    val queryParserType: QueryParserType = null
    val queryType: QueryType = null
    val writerType: WriterType = null
    val sort: Sort = null
    val startRow: StartRow = null
    val timeoutMilliseconds: TimeoutMilliseconds = null
    val version: Version = null
    val instance = new QueryRequest(core, explainOther, fieldsToReturn, filterQuery, isIndentEnabled, isDebugQueryEnabled, isEchoHandlerEnabled, isOmitHeaderEnabled, maximumRowsReturned, query, queryParserType, queryType, writerType, sort, startRow, timeoutMilliseconds, version)
    instance should not be null
  }

}
