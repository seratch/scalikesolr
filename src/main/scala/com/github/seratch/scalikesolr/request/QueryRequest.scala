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

package com.github.seratch.scalikesolr.request

import common.{ RequestParam, WriterType }
import reflect.BeanProperty
import query._
import distributedsearch.DistributedSearchParams
import facet.FacetParams
import group.GroupParams
import morelikethis.MoreLikeThisParams
import query.highlighting.HighlightingParams
import com.github.seratch.scalikesolr.SolrCore
import util.QueryStringUtil
import java.net.URLEncoder

case class QueryRequest(@BeanProperty var core: SolrCore = SolrCore(),
    @BeanProperty var explainOther: ExplainOther = ExplainOther(),
    @BeanProperty var fieldsToReturn: FieldsToReturn = FieldsToReturn(),
    @BeanProperty var filterQuery: FilterQuery = FilterQuery(),
    @BeanProperty var isIndentEnabled: IsIndentEnabled = IsIndentEnabled(),
    @BeanProperty var isDebugQueryEnabled: IsDebugQueryEnabled = IsDebugQueryEnabled(),
    @BeanProperty var isEchoHandlerEnabled: IsEchoHandlerEnabled = IsEchoHandlerEnabled(),
    @BeanProperty var isOmitHeaderEnabled: IsOmitHeaderEnabled = IsOmitHeaderEnabled(),
    @BeanProperty var maximumRowsReturned: MaximumRowsReturned = MaximumRowsReturned(),
    @BeanProperty var query: Query,
    @BeanProperty var queryParserType: QueryParserType = QueryParserType(),
    @BeanProperty var queryType: QueryType = QueryType(),
    @BeanProperty var writerType: WriterType = WriterType(),
    @BeanProperty var sort: Sort = Sort(),
    @BeanProperty var startRow: StartRow = StartRow(),
    @BeanProperty var timeoutMilliseconds: TimeoutMilliseconds = TimeoutMilliseconds(),
    @BeanProperty var version: Version = Version()) {

  @BeanProperty var echoParams: EchoParams = EchoParams()

  @BeanProperty var facet: FacetParams = FacetParams()

  @BeanProperty var group: GroupParams = GroupParams()

  @BeanProperty var highlighting: HighlightingParams = HighlightingParams()

  @BeanProperty var moreLikeThis: MoreLikeThisParams = MoreLikeThisParams()

  @BeanProperty var shards: DistributedSearchParams = DistributedSearchParams()

  private val extraParams = new collection.mutable.HashMap[String, Any]

  def set(key: String, value: Any) = extraParams.update(key, value)

  def remove(key: String) = extraParams.remove(key)

  def this(query: Query) = {
    this(
      core = SolrCore(""),
      explainOther = ExplainOther(),
      fieldsToReturn = FieldsToReturn(),
      filterQuery = FilterQuery(),
      isIndentEnabled = IsIndentEnabled(),
      isDebugQueryEnabled = IsDebugQueryEnabled(),
      isEchoHandlerEnabled = IsEchoHandlerEnabled(),
      isOmitHeaderEnabled = IsOmitHeaderEnabled(),
      maximumRowsReturned = MaximumRowsReturned(),
      query = query,
      queryType = QueryType(),
      queryParserType = QueryParserType(),
      writerType = WriterType(),
      sort = Sort(),
      startRow = StartRow(),
      timeoutMilliseconds = TimeoutMilliseconds(),
      version = Version()
    )
  }

  def this(core: SolrCore, query: Query) = {
    this(
      core = core,
      explainOther = ExplainOther(),
      fieldsToReturn = FieldsToReturn(),
      filterQuery = FilterQuery(),
      isIndentEnabled = IsIndentEnabled(),
      isDebugQueryEnabled = IsDebugQueryEnabled(),
      isEchoHandlerEnabled = IsEchoHandlerEnabled(),
      isOmitHeaderEnabled = IsOmitHeaderEnabled(),
      maximumRowsReturned = MaximumRowsReturned(),
      query = query,
      queryType = QueryType(),
      queryParserType = QueryParserType(),
      writerType = WriterType(),
      sort = Sort(),
      startRow = StartRow(),
      timeoutMilliseconds = TimeoutMilliseconds(),
      version = Version()
    )
  }

  def queryString(): String = {
    val buf = new StringBuilder
    appendIfExists(buf, this.echoParams)
    appendIfExists(buf, this.explainOther)
    appendIfExists(buf, this.fieldsToReturn)
    appendIfExists(buf, this.filterQuery)
    appendIfExists(buf, this.isIndentEnabled)
    appendIfExists(buf, this.isDebugQueryEnabled)
    appendIfExists(buf, this.isEchoHandlerEnabled)
    appendIfExists(buf, this.isOmitHeaderEnabled)
    appendIfExists(buf, this.maximumRowsReturned)
    appendIfExists(buf, this.query)
    appendIfExists(buf, this.queryType)
    appendIfExists(buf, this.queryParserType)
    appendIfExists(buf, this.writerType)
    appendIfExists(buf, this.sort)
    appendIfExists(buf, this.startRow)
    appendIfExists(buf, this.timeoutMilliseconds)
    appendIfExists(buf, this.version)
    if (this.group.enabled) {
      if (buf.length > 0) buf.append("&")
      buf.append("group=true")
      appendIfExists(buf, this.group.cachePercent)
      appendIfExists(buf, this.group.field)
      appendIfExists(buf, this.group.format)
      appendIfExists(buf, this.group.groupSort)
      appendIfExists(buf, this.group.limit)
      appendIfExists(buf, this.group.main)
      appendIfExists(buf, this.group.ngroups)
      appendIfExists(buf, this.group.offset)
      appendIfExists(buf, this.group.query)
      appendIfExists(buf, this.group.rows)
      appendIfExists(buf, this.group.sort)
      appendIfExists(buf, this.group.start)
    }
    if (this.highlighting.enabled) {
      if (buf.length > 0) buf.append("&")
      buf.append("hl=true")
      appendIfExists(buf, this.highlighting.alterField)
      appendIfExists(buf, this.highlighting.formatter)
      appendIfExists(buf, this.highlighting.fragListBuilder)
      appendIfExists(buf, this.highlighting.fragmenter)
      appendIfExists(buf, this.highlighting.fragmentsBuilder)
      appendIfExists(buf, this.highlighting.fragsize)
      appendIfExists(buf, this.highlighting.fieldsHighlighted)
      appendIfExists(buf, this.highlighting.isFastVectorHighlighterEnabled)
      appendIfExists(buf, this.highlighting.isFieldMatchRequred)
      appendIfExists(buf, this.highlighting.isMergeContiguousEnabled)
      appendIfExists(buf, this.highlighting.isMultiTermHighlightingEnabled)
      appendIfExists(buf, this.highlighting.isPhraseHighlighterEnabled)
      appendIfExists(buf, this.highlighting.maxAlternateFieldLength)
      appendIfExists(buf, this.highlighting.maxAnalyzedChars)
      appendIfExists(buf, this.highlighting.numOfSnippets)
      appendIfExists(buf, this.highlighting.regexpFragmenterSlop)
      appendIfExists(buf, this.highlighting.regexpMaxAnalyzedChars)
      appendIfExists(buf, this.highlighting.simplePrefix)
      appendIfExists(buf, this.highlighting.simpleSuffix)
    }
    if (this.moreLikeThis.enabled) {
      if (buf.length > 0) buf.append("&")
      buf.append("mlt=true&mlt.count=")
      buf.append(this.moreLikeThis.count)
      appendIfExists(buf, this.moreLikeThis.isBoostedQueryEnabled)
      appendIfExists(buf, this.moreLikeThis.maximumNumberOfQueryTerms)
      appendIfExists(buf, this.moreLikeThis.maximumNumberOfTokensToParseInEachDocument)
      appendIfExists(buf, this.moreLikeThis.maximumWordLengthAboveToBeIgnored)
      appendIfExists(buf, this.moreLikeThis.minimumDocumentFrequency)
      appendIfExists(buf, this.moreLikeThis.minimumTermFrequency)
      appendIfExists(buf, this.moreLikeThis.minimumWordLengthBelowToBeIgnored)
      appendIfExists(buf, this.moreLikeThis.fieldsToUseForSimilarity)
      appendIfExists(buf, this.moreLikeThis.queryFields)
    }
    if (this.facet.enabled) {
      if (buf.length > 0) buf.append("&")
      buf.append(this.facet.toQueryString)
    }
    val shards = this.shards.toQueryString
    if (!shards.isEmpty) {
      if (buf.length > 0) buf.append("&")
      buf.append(this.shards.toQueryString)
    }
    if (extraParams.size > 0) {
      extraParams.keys.foreach {
        key =>
          if (buf.length > 0) buf.append("&")
          buf.append(key)
          buf.append("=")
          buf.append(URLEncoder.encode(extraParams.getOrElse(key, "").toString, "UTF-8"))
      }
    }
    "?" + buf.toString
  }

  private def appendIfExists[RP <: RequestParam](buf: StringBuilder, param: RP): Unit = {
    QueryStringUtil.appendIfExists(buf, param)
  }

}