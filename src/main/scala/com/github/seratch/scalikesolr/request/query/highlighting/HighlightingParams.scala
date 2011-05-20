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
    this (
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

  override def toQueryString() = "hl.fl=" + fl

}

object FieldsHighlighted {
  def as(fl: String) = FieldsHighlighted(fl)
}

case class NumOfSnippets(@BeanProperty val snippets: Int = 1) extends RequestParam {

  override def isEmpty() = snippets == 1

  override def toQueryString() = "hl.snippets=" + snippets

}

object NumOfSnippets {
  def as(snippets: Int) = NumOfSnippets(snippets)
}

case class FragSize(@BeanProperty val fragsize: Int = 100) extends RequestParam {

  override def isEmpty() = fragsize == 100

  override def toQueryString() = "hl.fragsize=" + fragsize

}

object FragSize {
  def as(fragsize: Int) = FragSize(fragsize)
}

case class IsMergeContiguousEnabled(@BeanProperty val mergeContiguous: Boolean = false) extends RequestParam {

  override def isEmpty() = !mergeContiguous

  override def toQueryString() = "hl.mergeContiguous=" + mergeContiguous

}

object IsMergeContiguousEnabled {
  def as(mergeContiguous: Boolean) = IsMergeContiguousEnabled(mergeContiguous)
}

case class IsFieldMatchEnabled(@BeanProperty val requireFieldMatch: Boolean = false) extends RequestParam {

  override def isEmpty() = !requireFieldMatch

  override def toQueryString() = "hl.requireFieldMatch=" + requireFieldMatch

}

object IsFieldMatchEnabled {
  def as(requireFieldMatch: Boolean) = IsFieldMatchEnabled(requireFieldMatch)
}

case class MaxAnalyzedChars(@BeanProperty val maxAnalyzedChars: Int = 51200) extends RequestParam {

  override def isEmpty() = maxAnalyzedChars == 51200

  override def toQueryString() = "hl.maxAnalyzedChars=" + maxAnalyzedChars

}

object MaxAnalyzedChars {
  def as(maxAnalyzedChars: Int) = MaxAnalyzedChars(maxAnalyzedChars)
}

case class AlternateField(@BeanProperty val alternateField: String = "") extends RequestParam {

  override def isEmpty() = alternateField == null || alternateField.isEmpty

  override def toQueryString() = "hl.alternateField=" + alternateField

}

object AlternateField {
  def as(alternateField: String) = AlternateField(alternateField)
}

case class MaxAlternateFieldLength(@BeanProperty val maxAlternateFieldLength: Int = -1) extends RequestParam {

  override def isEmpty() = maxAlternateFieldLength == -1

  override def toQueryString() = "hl.maxAlternateFieldLength=" + maxAlternateFieldLength

}

object MaxAlternateFieldLength {
  def as(maxAlternateFieldLength: Int) = MaxAlternateFieldLength(maxAlternateFieldLength)
}

case class Formatter(@BeanProperty val formatter: String = "simple") extends RequestParam {

  override def isEmpty() = formatter == null || formatter.isEmpty || formatter == "simple"

  override def toQueryString() = "hl.formatter=" + formatter

}

object Formatter {
  def as(formatter: String) = Formatter(formatter)
}

case class SimplePrefix(@BeanProperty val simplePre: String = "<em>") extends RequestParam {

  override def isEmpty() = simplePre == null || simplePre.isEmpty || simplePre == "<em>"

  override def toQueryString() = "hl.simple.pre=" + simplePre

}

object SimplePrefix {
  def as(simplePre: String) = SimplePrefix(simplePre)
}

case class SimpleSuffix(@BeanProperty val simplePost: String = "</em>") extends RequestParam {

  override def isEmpty() = simplePost == null || simplePost.isEmpty || simplePost == "</em>"

  override def toQueryString() = "hl.simple.post=" + simplePost

}

object SimpleSuffix {
  def as(simplePost: String) = SimpleSuffix(simplePost)
}

case class Fragmenter(@BeanProperty val fragmenter: String = "gap") extends RequestParam {

  override def isEmpty() = fragmenter == null || fragmenter.isEmpty || fragmenter == "gap"

  override def toQueryString() = "hl.fragmenter=" + fragmenter

}

object Fragmenter {
  def as(fragmenter: String) = Fragmenter(fragmenter)
}

case class FragListBuilder(@BeanProperty val fragListBuilder: String = "") extends RequestParam {

  override def isEmpty() = fragListBuilder == null || fragListBuilder.isEmpty

  override def toQueryString() = "hl.fragListBuilder=" + fragListBuilder

}

object FragListBuilder {
  def as(fragListBuilder: String) = FragListBuilder(fragListBuilder)
}

case class FragmentsBuilder(@BeanProperty val fragmentsBuilder: String = "") extends RequestParam {

  override def isEmpty() = fragmentsBuilder == null || fragmentsBuilder.isEmpty

  override def toQueryString() = "hl.fragmentsBuilder=" + fragmentsBuilder

}

object FragmentsBuilder {
  def as(fragmentsBuilder: String) = FragmentsBuilder(fragmentsBuilder)
}

case class IsFastVectorHigighterEnabled(@BeanProperty val useFastVectorHigighter: Boolean = false) extends RequestParam {

  override def isEmpty() = !useFastVectorHigighter

  override def toQueryString() = "hl.useFastVectorHigighter=" + useFastVectorHigighter

}

object IsFastVectorHigighterEnabled {
  def as(useFastVectorHigighter: Boolean) = IsFastVectorHigighterEnabled(useFastVectorHigighter)
}

case class IsPhraseHigighterEnabled(@BeanProperty val usePhraseHigighter: Boolean = false) extends RequestParam {

  override def isEmpty() = !usePhraseHigighter

  override def toQueryString() = "hl.usePhraseHigighter=" + usePhraseHigighter

}

object IsPhraseHigighterEnabled {
  def as(usePhraseHigighter: Boolean) = IsPhraseHigighterEnabled(usePhraseHigighter)
}

case class IsMultiTermHighlightingEnabled(@BeanProperty val higightMultiTerm: Boolean = false) extends RequestParam {

  override def isEmpty() = !higightMultiTerm

  override def toQueryString() = "hl.higightMultiTerm=" + higightMultiTerm

}

object IsMultiTermHighlightingEnabled {
  def as(higightMultiTerm: Boolean) = IsMultiTermHighlightingEnabled(higightMultiTerm)
}

case class RegexFragmenterSlop(@BeanProperty val regexSlop: Double = 0.6) extends RequestParam {

  override def isEmpty() = regexSlop == 0.6

  override def toQueryString() = "hl.regex.slop=" + regexSlop

}

object RegexFragmenterSlop {
  def as(regexSlop: Double) = RegexFragmenterSlop(regexSlop)
}

case class RegexMaxAnalyzedChars(@BeanProperty val regexMaxAnalyzedChars: Int = 10000) extends RequestParam {

  override def isEmpty() = regexMaxAnalyzedChars == 10000

  override def toQueryString() = "hl.regex.maxAnalyzedChars=" + regexMaxAnalyzedChars

}

object RegexMaxAnalyzedChars {
  def as(regexMaxAnalyzedChars: Int) = RegexMaxAnalyzedChars(regexMaxAnalyzedChars)
}

