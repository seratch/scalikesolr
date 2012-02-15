package com.github.seratch.scalikesolr.request.query.morelikethis

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MoreLikeThisParamsSpec extends FlatSpec with ShouldMatchers {

  behavior of "MoreLikeThisParams"

  it should "be available" in {
    val enabled: Boolean = false
    val count: Int = 0
    val fieldsToUseForSimilarity: FieldsToUseForSimilarity = null
    val isBoostedQueryEnabled: IsBoostedQueryEnabled = null
    val maximumNumberOfQueryTerms: MaximumNumberOfQueryTerms = null
    val maximumNumberOfTokensToParseInEachDocument: MaximumNumberOfTokensToParseInEachDocument = null
    val maximumWordLengthAboveToBeIgnored: MaximumWordLengthAboveToBeIgnored = null
    val minimumDocumentFrequency: MinimumDocumentFrequency = null
    val minimumTermFrequency: MinimumTermFrequency = null
    val minimumWordLengthBelowToBeIgnored: MinimumWordLengthBelowToBeIgnored = null
    val queryFields: QueryFields = null
    val instance = new MoreLikeThisParams(enabled, count, fieldsToUseForSimilarity, isBoostedQueryEnabled, maximumNumberOfQueryTerms, maximumNumberOfTokensToParseInEachDocument, maximumWordLengthAboveToBeIgnored, minimumDocumentFrequency, minimumTermFrequency, minimumWordLengthBelowToBeIgnored, queryFields)
    instance should not be null
  }

}
