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

package com.github.seratch.scalikesolr.request.query.highlighting

import reflect.BeanProperty
import com.github.seratch.scalikesolr.request.common.RequestParam

case class HighlightingParams(@BeanProperty var enabled: Boolean = false,
    @BeanProperty var alterField: AlternateField = AlternateField(),
    @BeanProperty var fieldsHighlighted: FieldsHighlighted = FieldsHighlighted(),
    @BeanProperty var formatter: Formatter = Formatter(),
    @BeanProperty var fragListBuilder: FragListBuilder = FragListBuilder(),
    @BeanProperty var fragmenter: Fragmenter = Fragmenter(),
    @BeanProperty var fragmentsBuilder: FragmentsBuilder = FragmentsBuilder(),
    @BeanProperty var fragsize: FragSize = FragSize(),
    @BeanProperty var isFastVectorHigighterEnabled: IsFastVectorHigighterEnabled = IsFastVectorHigighterEnabled(),
    @BeanProperty var isFieldMatchRequred: IsFieldMatchEnabled = IsFieldMatchEnabled(),
    @BeanProperty var isMergeContiguousEnabled: IsMergeContiguousEnabled = IsMergeContiguousEnabled(),
    @BeanProperty var isMultiTermHighlightingEnabled: IsMultiTermHighlightingEnabled = IsMultiTermHighlightingEnabled(),
    @BeanProperty var isPhraseHigighterEnabled: IsPhraseHigighterEnabled = IsPhraseHigighterEnabled(),
    @BeanProperty var maxAlternateFieldLength: MaxAlternateFieldLength = MaxAlternateFieldLength(),
    @BeanProperty var maxAnalyzedChars: MaxAnalyzedChars = MaxAnalyzedChars(),
    @BeanProperty var numOfSnippets: NumOfSnippets = NumOfSnippets(),
    @BeanProperty var regexpFragmenterSlop: RegexFragmenterSlop = RegexFragmenterSlop(),
    @BeanProperty var regexpMaxAnalyzedChars: RegexMaxAnalyzedChars = RegexMaxAnalyzedChars(),
    @BeanProperty var simplePrefix: SimplePrefix = SimplePrefix(),
    @BeanProperty var simpleSuffix: SimpleSuffix = SimpleSuffix()) {

  def this(enabled: Boolean) {
    this(
      enabled = enabled,
      alterField = AlternateField(),
      fieldsHighlighted = FieldsHighlighted(),
      formatter = Formatter(),
      fragListBuilder = FragListBuilder(),
      fragmenter = Fragmenter(),
      fragmentsBuilder = FragmentsBuilder(),
      fragsize = FragSize(),
      isFastVectorHigighterEnabled = IsFastVectorHigighterEnabled(),
      isFieldMatchRequred = IsFieldMatchEnabled(),
      isMergeContiguousEnabled = IsMergeContiguousEnabled(),
      isMultiTermHighlightingEnabled = IsMultiTermHighlightingEnabled(),
      isPhraseHigighterEnabled = IsPhraseHigighterEnabled(),
      maxAlternateFieldLength = MaxAlternateFieldLength(),
      maxAnalyzedChars = MaxAnalyzedChars(),
      numOfSnippets = NumOfSnippets(),
      regexpFragmenterSlop = RegexFragmenterSlop(),
      regexpMaxAnalyzedChars = RegexMaxAnalyzedChars(),
      simplePrefix = SimplePrefix(),
      simpleSuffix = SimpleSuffix())
  }

}

object HighlightingParams {
  def as(enabled: Boolean): HighlightingParams = {
    new HighlightingParams(enabled)
  }
}

case class FieldsHighlighted(@BeanProperty val fl: String = "*") extends RequestParam {

  override def isEmpty() = fl == null || fl.isEmpty

  override def getKey() = "hl.fl"

  override def getValue() = fl.toString

}

object FieldsHighlighted {
  def as(fl: String) = FieldsHighlighted(fl)
}

case class NumOfSnippets(@BeanProperty val snippets: Int = 1) extends RequestParam {

  override def isEmpty() = snippets == 1

  override def getKey() = "hl.snippets"

  override def getValue() = snippets.toString

}

object NumOfSnippets {
  def as(snippets: Int) = NumOfSnippets(snippets)
}

case class FragSize(@BeanProperty val fragsize: Int = 100) extends RequestParam {

  override def isEmpty() = fragsize == 100

  override def getKey() = "hl.fragsize"

  override def getValue() = fragsize.toString

}

object FragSize {
  def as(fragsize: Int) = FragSize(fragsize)
}

case class IsMergeContiguousEnabled(@BeanProperty val mergeContiguous: Boolean = false) extends RequestParam {

  override def isEmpty() = !mergeContiguous

  override def getKey() = "hl.mergeContiguous"

  override def getValue() = mergeContiguous.toString

}

object IsMergeContiguousEnabled {
  def as(mergeContiguous: Boolean) = IsMergeContiguousEnabled(mergeContiguous)
}

case class IsFieldMatchEnabled(@BeanProperty val requireFieldMatch: Boolean = false) extends RequestParam {

  override def isEmpty() = !requireFieldMatch

  override def getKey() = "hl.requireFieldMatch"

  override def getValue() = requireFieldMatch.toString

}

object IsFieldMatchEnabled {
  def as(requireFieldMatch: Boolean) = IsFieldMatchEnabled(requireFieldMatch)
}

case class MaxAnalyzedChars(@BeanProperty val maxAnalyzedChars: Int = 51200) extends RequestParam {

  override def isEmpty() = maxAnalyzedChars == 51200

  override def getKey() = "hl.maxAnalyzedChars"

  override def getValue() = maxAnalyzedChars.toString

}

object MaxAnalyzedChars {
  def as(maxAnalyzedChars: Int) = MaxAnalyzedChars(maxAnalyzedChars)
}

case class AlternateField(@BeanProperty val alternateField: String = "") extends RequestParam {

  override def isEmpty() = alternateField == null || alternateField.isEmpty

  override def getKey() = "hl.alternateField"

  override def getValue() = toString(alternateField)

}

object AlternateField {
  def as(alternateField: String) = AlternateField(alternateField)
}

case class MaxAlternateFieldLength(@BeanProperty val maxAlternateFieldLength: Int = -1) extends RequestParam {

  override def isEmpty() = maxAlternateFieldLength == -1

  override def getKey() = "hl.maxAlternateFieldLength"

  override def getValue() = toString(maxAlternateFieldLength)

}

object MaxAlternateFieldLength {
  def as(maxAlternateFieldLength: Int) = MaxAlternateFieldLength(maxAlternateFieldLength)
}

case class Formatter(@BeanProperty val formatter: String = "simple") extends RequestParam {

  override def isEmpty() = formatter == null || formatter.isEmpty || formatter == "simple"

  override def getKey() = "hl.formatter"

  override def getValue() = toString(formatter)

}

object Formatter {
  def as(formatter: String) = Formatter(formatter)
}

case class SimplePrefix(@BeanProperty val simplePre: String = "<em>") extends RequestParam {

  override def isEmpty() = simplePre == null || simplePre.isEmpty || simplePre == "<em>"

  override def getKey() = "hl.simple.pre"

  override def getValue() = toString(simplePre)

}

object SimplePrefix {
  def as(simplePre: String) = SimplePrefix(simplePre)
}

case class SimpleSuffix(@BeanProperty val simplePost: String = "</em>") extends RequestParam {

  override def isEmpty() = simplePost == null || simplePost.isEmpty || simplePost == "</em>"

  override def getKey() = "hl.simple.post"

  override def getValue() = toString(simplePost)

}

object SimpleSuffix {
  def as(simplePost: String) = SimpleSuffix(simplePost)
}

case class Fragmenter(@BeanProperty val fragmenter: String = "gap") extends RequestParam {

  override def isEmpty() = fragmenter == null || fragmenter.isEmpty || fragmenter == "gap"

  override def getKey() = "hl.fragmenter"

  override def getValue() = toString(fragmenter)

}

object Fragmenter {
  def as(fragmenter: String) = Fragmenter(fragmenter)
}

case class FragListBuilder(@BeanProperty val fragListBuilder: String = "") extends RequestParam {

  override def isEmpty() = fragListBuilder == null || fragListBuilder.isEmpty

  override def getKey() = "hl.fragListBuilder"

  override def getValue() = toString(fragListBuilder)

}

object FragListBuilder {
  def as(fragListBuilder: String) = FragListBuilder(fragListBuilder)
}

case class FragmentsBuilder(@BeanProperty val fragmentsBuilder: String = "") extends RequestParam {

  override def isEmpty() = fragmentsBuilder == null || fragmentsBuilder.isEmpty

  override def getKey() = "hl.fragmentsBuilder"

  override def getValue() = toString(fragmentsBuilder)

}

object FragmentsBuilder {
  def as(fragmentsBuilder: String) = FragmentsBuilder(fragmentsBuilder)
}

case class IsFastVectorHigighterEnabled(@BeanProperty val useFastVectorHigighter: Boolean = false) extends RequestParam {

  override def isEmpty() = !useFastVectorHigighter

  override def getKey() = "hl.useFastVectorHigighter"

  override def getValue() = toString(useFastVectorHigighter)

}

object IsFastVectorHigighterEnabled {
  def as(useFastVectorHigighter: Boolean) = IsFastVectorHigighterEnabled(useFastVectorHigighter)
}

case class IsPhraseHigighterEnabled(@BeanProperty val usePhraseHigighter: Boolean = false) extends RequestParam {

  override def isEmpty() = !usePhraseHigighter

  override def getKey() = "hl.usePhraseHigighter"

  override def getValue() = toString(usePhraseHigighter)

}

object IsPhraseHigighterEnabled {
  def as(usePhraseHigighter: Boolean) = IsPhraseHigighterEnabled(usePhraseHigighter)
}

case class IsMultiTermHighlightingEnabled(@BeanProperty val higightMultiTerm: Boolean = false) extends RequestParam {

  override def isEmpty() = !higightMultiTerm

  override def getKey() = "hl.higightMultiTerm"

  override def getValue() = toString(higightMultiTerm)

}

object IsMultiTermHighlightingEnabled {
  def as(higightMultiTerm: Boolean) = IsMultiTermHighlightingEnabled(higightMultiTerm)
}

case class RegexFragmenterSlop(@BeanProperty val regexSlop: Double = 0.6) extends RequestParam {

  override def isEmpty() = regexSlop == 0.6

  override def getKey() = "hl.regex.slop"

  override def getValue() = toString(regexSlop)

}

object RegexFragmenterSlop {
  def as(regexSlop: Double) = RegexFragmenterSlop(regexSlop)
}

case class RegexMaxAnalyzedChars(@BeanProperty val regexMaxAnalyzedChars: Int = 10000) extends RequestParam {

  override def isEmpty() = regexMaxAnalyzedChars == 10000

  override def getKey() = "hl.regex.maxAnalyzedChars"

  override def getValue() = toString(regexMaxAnalyzedChars)

}

object RegexMaxAnalyzedChars {
  def as(regexMaxAnalyzedChars: Int) = RegexMaxAnalyzedChars(regexMaxAnalyzedChars)
}

