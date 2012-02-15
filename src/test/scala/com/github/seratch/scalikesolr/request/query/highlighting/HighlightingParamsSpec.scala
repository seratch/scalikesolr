package com.github.seratch.scalikesolr.request.query.highlighting

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class HighlightingParamsSpec extends FlatSpec with ShouldMatchers {

  behavior of "HighlightingParams"

  it should "be available" in {
    val enabled: Boolean = false
    val alterField: AlternateField = null
    val fieldsHighlighted: FieldsHighlighted = null
    val formatter: Formatter = null
    val fragListBuilder: FragListBuilder = null
    val fragmenter: Fragmenter = null
    val fragmentsBuilder: FragmentsBuilder = null
    val fragsize: FragSize = null
    val isFastVectorHighlighterEnabled: IsFastVectorHighlighterEnabled = null
    val isFieldMatchRequred: IsFieldMatchEnabled = null
    val isMergeContiguousEnabled: IsMergeContiguousEnabled = null
    val isMultiTermHighlightingEnabled: IsMultiTermHighlightingEnabled = null
    val isPhraseHighlighterEnabled: IsPhraseHighlighterEnabled = null
    val maxAlternateFieldLength: MaxAlternateFieldLength = null
    val maxAnalyzedChars: MaxAnalyzedChars = null
    val numOfSnippets: NumOfSnippets = null
    val regexpFragmenterSlop: RegexFragmenterSlop = null
    val regexpMaxAnalyzedChars: RegexMaxAnalyzedChars = null
    val simplePrefix: SimplePrefix = null
    val simpleSuffix: SimpleSuffix = null
    val instance = new HighlightingParams(enabled, alterField, fieldsHighlighted, formatter, fragListBuilder, fragmenter, fragmentsBuilder, fragsize, isFastVectorHighlighterEnabled, isFieldMatchRequred, isMergeContiguousEnabled, isMultiTermHighlightingEnabled, isPhraseHighlighterEnabled, maxAlternateFieldLength, maxAnalyzedChars, numOfSnippets, regexpFragmenterSlop, regexpMaxAnalyzedChars, simplePrefix, simpleSuffix)
    instance should not be null
  }

}
