package com.github.seratch.scalikesolr.request

import common.{RequestParam, WriterType}
import reflect.BeanProperty
import query._
import facet.FacetParams
import morelikethis.MoreLikeThisParams
import query.highlighting.HighlightingParams
import com.github.seratch.scalikesolr.SolrCore
import util.QueryStringUtil
case class QueryRequest(@BeanProperty var core: SolrCore = SolrCore(),
                        @BeanProperty var echoParams: EchoParams = EchoParams(),
                        @BeanProperty var explainOther: ExplainOther = ExplainOther(),
                        @BeanProperty var facet: FacetParams = FacetParams(),
                        @BeanProperty var fieldsToReturn: FieldsToReturn = FieldsToReturn(),
                        @BeanProperty var filterQuery: FilterQuery = FilterQuery(),
                        @BeanProperty var highlighting: HighlightingParams = HighlightingParams(),
                        @BeanProperty var isIndentEnabled: IsIndentEnabled = IsIndentEnabled(),
                        @BeanProperty var isDebugQueryEnabled: IsDebugQueryEnabled = IsDebugQueryEnabled(),
                        @BeanProperty var isEchoHandlerEnabled: IsEchoHandlerEnabled = IsEchoHandlerEnabled(),
                        @BeanProperty var isOmitHeaderEnabled: IsOmitHeaderEnabled = IsOmitHeaderEnabled(),
                        @BeanProperty var maximumRowsReturned: MaximumRowsReturned = MaximumRowsReturned(),
                        @BeanProperty var moreLikeThis: MoreLikeThisParams = MoreLikeThisParams(),
                        @BeanProperty var query: Query,
                        @BeanProperty var queryParserType: QueryParserType = QueryParserType(),
                        @BeanProperty var queryType: QueryType = QueryType(),
                        @BeanProperty var writerType: WriterType = WriterType(),
                        @BeanProperty var sort: Sort = Sort(),
                        @BeanProperty var startRow: StartRow = StartRow(),
                        @BeanProperty var timeoutMilliseconds: TimeoutMilliseconds = TimeoutMilliseconds(),
                        @BeanProperty var version: Version = Version()) {

  private val extraParams = new collection.mutable.HashMap[String, Any]

  def set(key: String, value: Any) = extraParams.update(key, value)

  def remove(key: String) = extraParams.remove(key)

  def this(query: Query) = {
    this (
      core = SolrCore(""),
      echoParams = EchoParams(),
      explainOther = ExplainOther(),
      facet = FacetParams(),
      fieldsToReturn = FieldsToReturn(),
      filterQuery = FilterQuery(),
      isIndentEnabled = IsIndentEnabled(),
      isDebugQueryEnabled = IsDebugQueryEnabled(),
      isEchoHandlerEnabled = IsEchoHandlerEnabled(),
      isOmitHeaderEnabled = IsOmitHeaderEnabled(),
      maximumRowsReturned = MaximumRowsReturned(),
      moreLikeThis = MoreLikeThisParams(),
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
    this (
      core = core,
      echoParams = EchoParams(),
      explainOther = ExplainOther(),
      facet = FacetParams(),
      fieldsToReturn = FieldsToReturn(),
      filterQuery = FilterQuery(),
      isIndentEnabled = IsIndentEnabled(),
      isDebugQueryEnabled = IsDebugQueryEnabled(),
      isEchoHandlerEnabled = IsEchoHandlerEnabled(),
      isOmitHeaderEnabled = IsOmitHeaderEnabled(),
      maximumRowsReturned = MaximumRowsReturned(),
      moreLikeThis = MoreLikeThisParams(),
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
      appendIfExists(buf, this.highlighting.isFastVectorHigighterEnabled)
      appendIfExists(buf, this.highlighting.isFieldMatchRequred)
      appendIfExists(buf, this.highlighting.isMergeContiguousEnabled)
      appendIfExists(buf, this.highlighting.isMultiTermHighlightingEnabled)
      appendIfExists(buf, this.highlighting.isPhraseHigighterEnabled)
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
    if (extraParams.size > 0) {
      extraParams.keys.foreach {
        key => {
          if (buf.length > 0) buf.append("&")
          buf.append(key)
          buf.append("=")
          buf.append(extraParams.getOrElse(key, ""))
        }
      }
    }
    "?" + buf.toString.replaceAll(" ", "%20")
  }

  private def appendIfExists[RP <: RequestParam](buf: StringBuilder, param: RP): Unit = {
    QueryStringUtil.appendIfExists(buf, param)
  }

}